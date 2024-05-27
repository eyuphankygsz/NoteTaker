# Note Taker

### Features

- Registration system,
- Adding friends, collabrates to your note,
- Saving notes,
- MySQL Database

### Database

I use MySQL for this project. It has 3 tables:
- User
- Notes
- Collabs

> Do not forget to implement the sql files.

## Implementation

### Database Implementation

- Open MySQL Structure Folder,
- Import .sql files to workbench.

### Project Implementation

- Open project in any Java IDE (I prefer Eclipse),
- Go to Project Libraries,
- Download [Java MySQL Connector](https://dev.mysql.com/downloads/connector/j/) here,
- Extract zip to a folder ,
- Go back to Project Libraries,
- Select mysql connector and edit,
- Go to extracted folder and select new mysqlconnector.jar,
- Apply and close,
- At the end:

```javascript
public Connection GetConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/notes", "root", "admin");
	}
```
- Find this Method and change the mysql information with your information.
- And run!
