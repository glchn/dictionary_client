package ca.ubc.cs317.dict.net;

import ca.ubc.cs317.dict.model.Database;
import ca.ubc.cs317.dict.model.Definition;
import ca.ubc.cs317.dict.model.MatchingStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;
import java.util.*;

import static ca.ubc.cs317.dict.net.DictStringParser.splitAtoms;

/**
 * Created by Jonatan on 2017-09-09.
 */
public class DictionaryConnection {

    private static final int DEFAULT_PORT = 2628;
    private Socket s;
    private BufferedReader in;
    private BufferedReader stdIn;
    private PrintWriter out;
    private Status status;

    /**
     * Establishes a new connection with a DICT server using an explicit host and port number, and handles initial
     * welcome messages.
     *
     * @param host Name of the host where the DICT server is running
     * @param port Port number used by the DICT server
     * @throws DictConnectionException If the host does not exist, the connection can't be established, or the messages
     *                                 don't match their expected value.
     */
    public DictionaryConnection(String host, int port) throws DictConnectionException {
        // TODO Add your code here
        // https://docs.oracle.com/javase/tutorial/networking/sockets/readingWriting.html
        try {
            s = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
            status = Status.readStatus(in);
            if (status.getStatusCode() != 220) {
                throw new DictConnectionException("Could not connect.");
            }
        } catch (Exception e) {
            throw new DictConnectionException();
        }
    }

    /**
     * Establishes a new connection with a DICT server using an explicit host, with the default DICT port number, and
     * handles initial welcome messages.
     *
     * @param host Name of the host where the DICT server is running
     * @throws DictConnectionException If the host does not exist, the connection can't be established, or the messages
     *                                 don't match their expected value.
     */
    public DictionaryConnection(String host) throws DictConnectionException {
        this(host, DEFAULT_PORT);
    }

    /**
     * Sends the final QUIT message and closes the connection with the server. This function ignores any exception that
     * may happen while sending the message, receiving its reply, or closing the connection.
     */
    public synchronized void close() {
        // TODO Add your code here
        try {
            // send cmd
            out.println("QUIT");
            // check that connection closed
            Status.readStatus(in);
            s.close();
        } catch (Exception e) {
        }
    }

    /**
     * Requests and retrieves all definitions for a specific word.
     *
     * @param word     The word whose definition is to be retrieved.
     * @param database The database to be used to retrieve the definition. A special database may be specified,
     *                 indicating either that all regular databases should be used (database name '*'), or that only
     *                 definitions in the first database that has a definition for the word should be used
     *                 (database '!').
     * @return A collection of Definition objects containing all definitions returned by the server.
     * @throws DictConnectionException If the connection was interrupted or the messages don't match their expected value.
     */
    public synchronized Collection<Definition> getDefinitions(String word, Database database) throws DictConnectionException {
        Collection<Definition> set = new ArrayList<>();

        // TODO Add your code here
        String cmd = "DEFINE " + database.getName() + " " + "\"" + word + "\"";
        out.println(cmd);
        try {
            status = Status.readStatus(in);
            // if 552 (no match) or 550 (invalid db), return empty definition set
            if (status.getStatusCode() == 552 || status.getStatusCode() == 550) {
                return set;
            } if (status.getStatusCode() == 150) { // 150 n definitions found: list follows
                String strings[] = splitAtoms(status.getDetails());
                int n = Integer.parseInt(strings[0]); // get the number of definitions
                int counter = 0;
                while (counter < n) { // while we haven't reached the last definition...
                    status = Status.readStatus(in);
                    if (status.getStatusCode() == 151) { // check 151 word database name - text follows
                        String arr[] = splitAtoms(status.getDetails()); // arr: word, database name, dict title(?)
                        Definition definition; // create new definition with word and database name
                        definition = new Definition(word, arr[1]);
                        String read = in.readLine(); // get next line of def
                        while (!read.equals(".")) {
                            definition.appendDefinition(read); // parse and add line to definition
                            read = in.readLine(); // retrieve next def
                        }
                        set.add(definition); // add definition to set
                        counter ++; // continue to next definition
                        }
                    }
                }
            status = Status.readStatus(in);
            if (status.getStatusCode() != 250) {
                set.clear();
            }
            return set; // if 250 (ok), return populated definition set
        } catch (IOException e) {
            throw new DictConnectionException();
        }
    }

    /**
     * Requests and retrieves a list of matches for a specific word pattern.
     *
     * @param word     The word whose definition is to be retrieved.
     * @param strategy The strategy to be used to retrieve the list of matches (e.g., prefix, exact).
     * @param database The database to be used to retrieve the definition. A special database may be specified,
     *                 indicating either that all regular databases should be used (database name '*'), or that only
     *                 matches in the first database that has a match for the word should be used (database '!').
     * @return A set of word matches returned by the server.
     * @throws DictConnectionException If the connection was interrupted or the messages don't match their expected value.
     */
    public synchronized Set<String> getMatchList(String word, MatchingStrategy strategy, Database database) throws DictConnectionException {
        Set<String> set = new LinkedHashSet<>();

        // TODO Add your code here
        String cmd = "MATCH " + database.getName() + " " + strategy.getName() + " " + "\"" + word + "\"";
        out.println(cmd);
        try {
            status = Status.readStatus(in);
            // if 550 (invalid db), 551 (invalid strat), 552 (no match), return empty set
            if (status.getStatusCode() == 550 || status.getStatusCode() == 551 || status.getStatusCode() == 552) {
                return set;
            }
            if (status.getStatusCode() == 152) {
                String read = in.readLine();
                while (!(read.equals("."))) { // while haven't reached final match
                    String[] strings = splitAtoms(read);
                    set.add(strings[1]); // add match to set
                    read = in.readLine(); // get next match
                }
            }
            status = Status.readStatus(in);
            // if status code is not 250, command not complete
            if (status.getStatusCode() != 250) {
                throw new DictConnectionException("Non-250 terminating status code");
            } else {
                return set; // if 250 (ok), return populated match set
            }
        } catch (IOException e) {
            throw new DictConnectionException();
        }
    }

    /**
     * Requests and retrieves a map of database name to an equivalent database object for all valid databases used in the server.
     *
     * @return A map of Database objects supported by the server.
     * @throws DictConnectionException If the connection was interrupted or the messages don't match their expected value.
     */
    public synchronized Map<String, Database> getDatabaseList() throws DictConnectionException {
        Map<String, Database> databaseMap = new HashMap<>();

        // TODO Add your code here
        // send command
        out.println("SHOW DB");
        try {
            status = Status.readStatus(in);
            if (status.getStatusCode() == 554) { // 554: if no db present, return empty dbmap
                return databaseMap;
            }
            if (status.getStatusCode() == 110) { // 110: n db present - text follows
                String read = in.readLine();
                // while not reached end of db list
                while (!(read.equals("."))) {
                    String[] strings = splitAtoms(read);
                    Database database = new Database(strings[0], strings[1]); // create db object with parsed name and description
                    databaseMap.put(database.getName(), database); // add db into map
                    read = in.readLine();
                }
            }
            status = Status.readStatus(in);
            if (status.getStatusCode() != 250) {
                throw new DictConnectionException("Non-250 terminating status code");
            }
            return databaseMap; // if 250 (ok), return populated db map
        } catch (IOException e) {
            throw new DictConnectionException();
        }
    }


    /**
     * Requests and retrieves a list of all valid matching strategies supported by the server.
     *
     * @return A set of MatchingStrategy objects supported by the server.
     * @throws DictConnectionException If the connection was interrupted or the messages don't match their expected value.
     */
    public synchronized Set<MatchingStrategy> getStrategyList() throws DictConnectionException {
        Set<MatchingStrategy> set = new LinkedHashSet<>();

        // TODO Add your code here
        out.println("SHOW STRAT");
        try {
            status = Status.readStatus(in);
            if (status.getStatusCode() == 555) { // 555: no strat available, return empty set
                return set;
            }
            if (status.getStatusCode() == 111) { // 111: n strategies avilable, text follows
                String read = in.readLine();
                while (!(read.equals("."))) { // while we haven't reached final strategy
                    String[] strings = splitAtoms(read);
                    MatchingStrategy strat = new MatchingStrategy(strings[0], strings[1]); // create strategy
                    set.add(strat); // get next strategy
                    read = in.readLine(); // get next strategy
                }
            }
            status = Status.readStatus(in);
            if (status.getStatusCode() != 250) {
                throw new DictConnectionException();
            } else {
                return set; // if 250 (ok), return populated strat set
            }
        } catch (IOException e) {
            throw new DictConnectionException();
        }
    }
}