  <h4>Assignment Overview</h4>
  <p>In this assignment you will use the Java socket related classes
    to create a dictionary client. Your program is to provide some
    basic dictionary functionality through a simple graphical
    interface. You will implement a subset of the client side of the
    DICT protocol, described in RFC 2229, which can be found
    here: <a href="https://tools.ietf.org/html/rfc2229" target="_blank">https://tools.ietf.org/html/rfc2229</a>.</p>
  <p>To start your assignment, download the
    file <a href="/pl/course_instance/2374/instance_question/16517817/clientFilesQuestion/Dictionary.zip" download="">Dictionary.zip</a>. This
    file contains a directory called <code>Dictionary</code> which can
    be imported into IDEs like IntelliJ or Eclipse to develop your
    code.</p>
  <p>The file above contains a skeleton code that provides a
    user-interface for the functionality you are to implement. The
    interface, however, does not actually establish any connection or
    transfer any data. Your job is to implement the connection
    establishment and data transfer for this application. More
    specifically, you will need to implement the code that performs
    each of the following tasks:</p>
  <ul>
    <li>Establish a connection with a DICT server, and receive the
      initial welcome message.</li>
    <li><b>Do not send an initial CLIENT command to the server, the tests
      will fail if you do!</b></li>
    <li>Finish a connection with a DICT server by sending a final QUIT
      message, receiving its reply, and closing the socket
      connection.</li>
    <li>Requesting, receiving, parsing and returning a list of
      databases used in the server. Each database corresponds to one
      dictionary that can be used to retrieve definitions
      from. Examples include one or more regular English dictionaries,
      a Thesaurus, an English-French dictionary, a dictionary of
      technical terms, a dictionary of acronyms, etc. In the
      interface, the user will have the option of selecting a specific
      database, or all databases.</li>
    <li>Requesting, receiving, parsing and returning a list of
      matching strategies supported by the server. The protocol allows
      a client to retrieve a list of matches (suggestions) based on a
      keyword, and the strategy is used to indicate how these keywords
      are used to present actual dictionary entries. Examples include
      prefix matches (all words that start with a prefix), regular
      expressions, entries with similar sounding words, etc. In the
      interface, the user will have the option of selecting a specific
      strategy for matching, with prefix match being the default.</li>
    <li>Requesting, receiving, parsing and returning a list of matches
      based on a keyword, a matching strategy and a database. This
      list of matches will be used in the interface to suggest entries
      as they type a word.</li>
    <li>Requesting, receiving, parsing and returning a list of
      definitions for a word, based on a database. Each definition
      will correspond to the word, a database, and the definition
      itself, which may contain several lines. All definitions are
      listed in the interface in the order in which they are
      received.</li>
