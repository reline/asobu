<?php

//for database
define("DB_HOST", "localhost");
define("DB_NAME", "asobu_db_test");
define("DB_USER", "root");
define("DB_PASS", "enter");


function doDB() {
    global $mysqli;
    
    //connect to server and select database;
    //servername, user, passwd, dbname
    $mysqli = mysqli_connect(DB_HOST, DB_USER, DB_PASS, DB_NAME);
    //if connection fails, stop script
    if(mysqli_connect_errno()) {
        printf("Connection failed: %s\n", mysqli_connect_error($mysqli));
        exit();
    }
    
}

?>