package xyz.theprogramsrc.supercoreapi.global.storage;

import xyz.theprogramsrc.supercoreapi.SuperPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public abstract class SQLiteDataBase implements DataBase {

    protected Connection connection;
    private final String connectionURL;
    protected SuperPlugin<?> plugin;

    public SQLiteDataBase(SuperPlugin<?> plugin){
        this.plugin = plugin;
        this.connectionURL = "jdbc:sqlite:" + this.plugin.getPluginFolder() + File.separator + this.plugin.getPluginName().toLowerCase() + ".db";

        try{
            Class.forName("org.sqlite.JDBC");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * SQLite is always available
     * @return True because SQLite dont require internet
     */
    public boolean isLoaded() {
        return true;
    }

    /**
     * Closes the current connection with DataBase
     */
    public void closeConnection() {
        try{
            if(this.connection != null){
                this.connection.close();
            }
        }catch (Exception ex){
            this.plugin.log("&cCannot close SQLite Connection:");
            ex.printStackTrace();
        }
    }

    /**
     * Connects to the DataBase and execute the specified call
     * @param call ConnectionCall to execute
     */
    public void connect(ConnectionCall call) {
        if(this.connection == null){
            try{
                this.connection = DriverManager.getConnection(this.connectionURL);
            }catch (Exception ex){
                this.plugin.log("&cCannot connect with SQLite DataBase:");
                ex.printStackTrace();
            }
        }

        try{
            call.onConnect(this.connection);
        }catch (Exception ex){
            this.plugin.log("&cCannot execute ConnectionCall:");
            ex.printStackTrace();
        }
    }
}
