<?php

/*
This file is nothing. It doesn't exist for me anymore. u.u
*/

header("Content-Type: application/json");

include 'common.php';
doDB();

if($_GET['id']){
    $song_id = "%" . mysqli_real_escape_string($_GET['id']) . "%";
}
else{
    $song_id = "";
}

if($_GET['name']){
    $song_name = "%" . mysqli_real_escape_string($_GET['name']) . "%";
}
else{
    $song_name = "";
}

if($_GET['artist']){
    $song_artist = "%" . mysqli_real_escape_string($_GET['artist']) . "%";
}
else{
    $song_artist = "";
}

if($_GET['album']){
    $song_album = "%" . mysqli_real_escape_string($_GET['album']) . "%";
}
else{
    $song_album = " ";
}

if($_GET['genre']){
    $song_genre = "%" . mysqli_real_escape_string($_GET['genre']) . "%";
}
else{
    $song_genre = "";
}

if($_GET['length']){
    $song_length = "%" . mysqli_real_escape_string($_GET['length']) . "%";
}
else{
    $song_length = "";
}

if($_GET['diff']){
    $song_diff = "%" . mysqli_real_escape_string($_GET['diff']) . "%";
}
else{
    $song_diff = "";
}

if($_GET['artwork']){
    $album_artwork = "%" . mysqli_real_escape_string($_GET['artwork']) . "%";
}
else{
    $album_artwork = "";
}

if($_GET['sortby']){
    $orderby = "%" . mysqli_real_escape_string($_GET['orderby']) . "%";
}
else{
    $orderby = "song_id";
}

if($_GET['acsdesc']) {
    $ascdesc = "%" . mysqli_real_escape_string($_GET['acsdesc']) . "%";
}
else{
    $ascdesc = "ASC";
}

//TODO: fix this...uuuuuuggggggghhhhhhhhhhh
$get_all_songs_query = "SELECT song_id, song_name, song_artist, song_album, song_genre, song_length, song_diff, album_artwork FROM Song WHERE song_id = '".$song_id."', song_name = '".$song_name."', song_artist = '".$song_artist."', song_album = '".$song_album."', song_genre = '".$song_genre."', song_length = '".$song_length."', song_diff = '".$song_diff."', album_artwork = '".$album_artwork."' ORDER BY ".$orderby. " " .$ascdesc;

$get_all_songs_response = mysqli_query($mysqli, $get_all_songs_query) or die(mysqli_error($mysqli));

$songdata = '[';

while($row = mysqli_fetch_assoc($get_all_songs_response)) {
    $songdata .= json_encode($row);
    $songdata .=",";
}
mysqli_free_result($get_all_songs_response);
mysqli_close($mysqli);

$songdata = rtrim($songdata, ",");
$songdata .= ']';

echo $songdata;



?>