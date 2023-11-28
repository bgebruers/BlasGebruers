const mysql = require("mysql");
const dbConfig = require("./db-config");    //asi se llama otro archivo en otro js, db-config tiene las consiguraciones de la bd

// Create a connection to the database.
const connection = mysql.createConnection({
  host: dbConfig.HOST,
  user: dbConfig.USER,
  password: dbConfig.PASSWORD,
  database: dbConfig.DB
});

// open the MySQL connection
connection.connect(error => {
  if (error) throw error;
  console.log("Successfully connected to the database.");
});

module.exports = connection;