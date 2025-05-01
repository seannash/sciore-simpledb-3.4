package simpledb.jdbc.network;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

class NetworkDataSource
{

    private final String base = "jdbc:simpledb://";
    private String host;
    private Properties prop;
    private NetworkDriver driver;

    NetworkDataSource() {
        this.host = "";
        this.prop = new Properties();
        this.driver = new NetworkDriver();
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setProperties(Properties prop) {
        this.prop = prop;
    }

    public Connection connect(String url, Properties prop) throws SQLException {
        return this.driver.connect(this.base + this.host, this.prop);
     }
}