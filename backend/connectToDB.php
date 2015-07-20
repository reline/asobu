<?php

//db congif
define('DB_HOST', 'localhost');
define('DB_NAME', 'asobu_db');
define('DB_USER', 'root');
define('DB_PASS', '');

/*
$DB_USER = "mena";
$DB_PASS = "tigers";
$DB_HOSTNAME = "localhost";
$DB_NAME = "asobu_db";
*/
    
function connectDB(){
    $db =  mysqli_connect(DB_HOST, DB_USER, DB_PASS, DB_NAME)
        or die("Cannot connect to database: " . mysqli_connect_errno());
    echo "Connected to database!";
    
    return $db;
    
}

//$conn = connectDB();

//mysqli_close($conn);


?>