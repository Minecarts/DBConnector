## DBConnector Plugin

This is just a small, quick dirty plugin that wraps the snaq DBPool utility to let us manage our own MySQL connection pool. 
Since most of our plugins will store information in MySQL, this provides a nice easy to use interface to the pool within Minecraft.

The Snaq DBPool driver can be found at: 
http://www.snaq.net/java/DBPool/  


## Example config.yml

    defaults:
        # default pool returned when getPool() is called with no/null arguments
        pool: mysql # pool name

        # default values for pools section
	    url: "jdbc:mysql://localhost:3306/minecraft"
	    username: "root"
	    password: ""
        minConn: 3
        maxConn: 5
        maxCreated: 7
        connTimeout: 3600 # seconds

    pools:
        mysql: # pool name
            url: "jdbc:mysql://localhost:3306/db"
            username: "user"
            password: "password"
            minConn: 3
            maxConn: 5
            maxCreated: 7
            connTimeout: 3600 # seconds
