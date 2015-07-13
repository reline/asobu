<?php

require('connectToDB.php');

//get post variables
$name = $_POST['song_name'];
$artist = $_POST['song_artist'];
$album = $_POST['song_album'];
$genre = $_POST['song_genre'];
$length = $_POST['song_length'];
$diff = $_POST['song_diff'];
//$album_art = $_POST['album_artwork'];

//connect to database
$conn = connectToDB();

$sqlquery = "INSERT INTO Song ('song_name', 'song_artist', 'song_album', 'song_genre', 'song_length', 'song_diff', 'album_artwork') VALUES('$name','$artist', '$album', '$genre', '$length', '$diff', null)";

$results = mysqli_query($conn, $sqlquery);

if(!$results) {
    die("Could not enter data: " . mysqli_error());
}
echo "records entered!";

mysqli_close($conn);

echo "song name: $name <br> song artist: $artist <br> song album: $album <br> song genre: $genre <br> song length: $length <br> song diffuculty: $diff <br>";


?>