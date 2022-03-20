<h4>Special Note</h4>
  <p>Although this assignment's autograder tests the most common input
    issues and produces an initial score, this score is going to be
    overwritten by a manual review by the course staff. This manual
    review will ensure your code works not only based on the simple
    cases listed in the resulting tests, but also for other
    considerations listed in the RFC and for additional tests. TAs
    will also review your code quality in terms of clarity and use of
    comments.</p>
  
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
  </ul>
  <p>Remember, you are only required to implement a subset of the
    protocol, so some of the material in the references goes beyond
    what you need. Keep in mind that the RFC describes the data
    (protocol) exchanges between the DICT client (i.e., what you are
    writing) and a DICT server.</p>
  <p>All the functionality listed above is based on the implementation
    of the constructor and methods of the
    class <code>ca.ubc.cs317.dict.net.DictionaryConnection</code>,
    available in the provided code. This is the only file you are
    allowed to change.</p>
  
  <h4>Implementation Constraints and Suggestions</h4>
  <p>You are strongly encouraged to test the connectivity and message
    formats of the DICT protocol using tools like netcat or
    telnet. Tutorial 1 discusses some of these tools.</p>
  <p>Don't try to implement this assignment all at once. Instead,
    incrementally build and simultaneously test the solution. You are
    strongly encouraged to use the order listed above. Doing so will
    allow you to test your implementation in a progressive manner,
    since some of the items depend on each other (e.g., you need to
    obtain the list of matching strategies before you can request for
    matches). A suggested strategy is to:</p>
  <ul>
    <li>Read the RFC to understand how the protocol works. Use netcat
      or telnet to test it and see it in action.</li>
    <li>Establish a connection with the server.</li>
    <li>Implement the features in the order listed above.</li>
  </ul>
  <p>You are not expected to implement any DICT command that is not
    listed, but you may do so if you want. Note that, since the user
    interface does not provide support for most of the additional
    commands available in the protocol, you may be required to change
    the UI to see these new features in action. Should you do so, it
    is your responsibility to ensure that, should the UI be changed
    back to its original version, your code continues to work as
    intended originally.</p>
  <p>Some commands and replies in DICT may contain more than one
    word. Your code must support such words both when reading replies
    (e.g., when retrieving matches) and when sending commands (e.g.,
    when requesting a definition). Note that these words are presented
    with quotes by the assignment; these quotes are not part of the
    keyword, and must not be included when returning their
    values. Note that a few classes are provided to help you handle
    status messages (class <code>Status</code>) and parsing lines into
    tokens (class <code>DictStringParser</code>). You are encouraged
    to use these classes as you see fit.</p>
  <p>Some replies in DICT may contain a sequence of lines terminated
    by a line containing only a period (<code>.</code>) symbol. Note
    that this period is not part of the data, and must not be included
    in your collected data. For example, a definition that ends with
    such a line should not include that line as part of the definition
    itself.</p>
  <p>For testing purposes you can connect to any DICT server, like
    dict.org. On some machines you might be able to run your own copy
    of a server with software
    like <a href="http://www.informatik.uni-leipzig.de/~duc/Java/JDictd/" target="_blank">JDictD</a> for additional testing, but this is not
    required.</p>
  <p>This is an individual assignment.  You will work by yourself.  You may
    discuss this assignment with your classmates, but you 
    must do all the work by yourself.  You are not allowed to
    show your code, even for discussion or explanatory purposes, to
    other classmates.</p>
  <p>Style and comments are part of the evaluation, so keep your code
    clear, clean and well-documented.</p>
  <p>You may include, at the top of
    your <code>DictionaryConnection.java</code> file, comments
    containing any information you would like to convey to the TAs
    about your assignment. For example maybe you didn't get some part
    of the assignment working, or it works in some circumstances but
    not others.</p>
