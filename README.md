## DBConnector Plugin

This is just a small, quick dirty plugin that wraps the snaq DBPool utility to let us manage our own MySQL connection pool. 
Since most of our plugins will store information in MySQL, this provides a nice easy to use interface to the pool within Minecraft.

The Snaq DBPool driver can be found at: 
http://www.snaq.net/java/DBPool/  


## Example config.yml

    # default provider name used when getProvider() is called with no arguments
    defaultProvider: mysql

    providers:
        mysql: # provider name
            type: MySQLPool
            url: "jdbc:mysql://localhost:3306/db"
            username: "user"
            password: "password"
            minConn: 3
            maxConn: 5
            maxCreated: 7
            connTimeout: 3600 # seconds
