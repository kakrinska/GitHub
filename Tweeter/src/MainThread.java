import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Date;

public class MainThread extends Thread {
	private final static String DB_URL = "jdbc:mysql://localhost:3306/mytweetdb";
	private final static String DB_USER = "root";
	private final static String DB_PASS = "123456";
	private final static int CLIENT_REQUEST_TIMEOUT = 15*60*1000;
	protected MyDbConnecton dbConnection;
	protected Socket socket;
	protected BufferedReader mSocketReader;
	protected PrintWriter mSocketWriter;
	protected ObjectInputStream objectInputStream;
	protected ObjectOutputStream objectOutputStream;
	private String username;
    
    public MainThread(Socket socket) throws IOException {
    	dbConnection = new MyDbConnecton(DB_URL, DB_USER, DB_PASS);
	    dbConnection.connect();
    	this.socket = socket;
    	socket.setSoTimeout(CLIENT_REQUEST_TIMEOUT);
    	mSocketReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
        mSocketWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run() {
    	System.out.println(new Date().toString() + " : " + 
                "Accepted client : " + socket.getInetAddress() + 
                ":" + socket.getPort()); 
    	try {
	    		menu();
    	} catch (Exception ex) { 
            System.out.println("Client loged out!");
    	} finally {
    		dbConnection.closeConnection();
    		try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    private void menu() throws ClassNotFoundException, IOException {
    	int statement = objectInputStream.readInt();
		switch(statement) {
		case 0 :
			menu();
			break;
		case 1 : 
			logIn();
			menu();
			break;
		case 2 :
			createAccount();
			menu();
			break;
		case 3 : 
			showMessages();
			menu();
			break;
		case 4 : 
			addMessage();
			menu();
			break;
		case 5 :
			getUsernames();
			menu();
			break;
		case 6 :
			setFollowings();
			menu();
			break;
		default : 
			break;
		}
    }
    
    void createAccount() {
    	try {
			String username = mSocketReader.readLine();
			String password = mSocketReader.readLine();
			if(dbConnection.checkUsername(username) == 1) {
				objectOutputStream.writeBoolean(false);
				objectOutputStream.flush();
			} else {
				dbConnection.setAccount(username, password);
				objectOutputStream.writeBoolean(true);
				objectOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
	void logIn() {
    	try {
    		username = mSocketReader.readLine();
			String password = mSocketReader.readLine();
			int userExist = dbConnection.checkUsername(username);
			String upassword = dbConnection.getPassword(username);
			if(userExist == 1 && upassword.equals(password) && !username.equals("") && !password.equals("")) {
		    	mSocketWriter.println(true);
		    	mSocketWriter.flush();
			} else {
				mSocketWriter.println(false);
		    	mSocketWriter.flush();
		    	username = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	void showMessages() {
    	try {
			objectOutputStream.writeObject(dbConnection.getMessages(username));
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void addMessage() {
		String messageToAdd;
		try {
			messageToAdd = (String) objectInputStream.readObject();
			dbConnection.setMessage(username, messageToAdd);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    }
	
	void getUsernames(){
		try {
			objectOutputStream.writeObject(dbConnection.getUsernames());
			objectOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void setFollowings() {
		String followingFor = null;
		try {
			followingFor = (String) objectInputStream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		dbConnection.setFollowings(username, followingFor);
	}
}
