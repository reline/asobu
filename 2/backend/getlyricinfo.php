<?php
header("Content-Type: application/json; charset=utf-8");

include 'common.php';

doDB();


if(!isset($_GET['lyric_id']) && !isset($_GET['song_id']) && !isset($_GET['format']) && !isset($_GET['limit'])){
    echo "LYRICS - look at the url and reevaluate your life";
    exit();
}

$safe_song_id = "";
$safe_lyric_id = "";
$safe_lyric_format = "";
$safe_limit = "";

if(isset($_GET['lyric_id'])){
    $safe_lyric_id = mysqli_real_escape_string($mysqli, $_GET['lyric_id']);
}
if(isset($_GET['song_id'])){
    $safe_song_id = mysqli_real_escape_string($mysqli, $_GET['song_id']);
}
if(isset($_GET['format'])){
    $safe_lyric_format = mysqli_real_escape_string($mysqli, $_GET['format']);
}
if(isset($_GET['limit'])){
    $safe_limit = mysqli_real_escape_string($mysqli, $_GET['limit']);
}


if(!$safe_lyric_id && !$safe_song_id && !$safe_lyric_format && !$safe_limit){
    echo "LYRICS - bruh you're not even searching for anything...";
    exit();
}

if(!$safe_lyric_id && !$safe_song_id && !$safe_lyric_format && $safe_limit){
    echo "LYRICS - bruh I'm gonna need more than that...";
    exit();
}

/*
else if(!$safe_lyric_id && !$safe_song_id ) {
    echo "LYRICS - almost there...pls provide an id";
    exit();
}
*/

$get_lyric_info_query = "SELECT lyric_id, Lyrics.song_id, song_name, format, link_to_file FROM Lyrics join Song WHERE Lyrics.song_id = Song.song_id AND";

$query_string = "";

/*
if($safe_lyric_id && !$safe_song_id) {
    $get_lyric_info_query = "SELECT lyric_id, Lyrics.song_id, song_name, format, link_to_file FROM Lyrics join Song WHERE lyric_id = '" . $safe_lyric_id . "' AND Lyrics.song_id = Song.song_id";
}
else if(!$safe_lyric_id && $safe_song_id) {
    $get_lyric_info_query = "SELECT lyric_id, Lyrics.song_id, song_name, format, link_to_file FROM Lyrics join Song WHERE Lyrics.song_id = '" . $safe_song_id. "' AND Lyrics.song_id = Song.song_id";
}
else if($safe_lyric_id && $safe_song_id) {
    $get_lyric_info_query = "SELECT lyric_id, Lyrics.song_id, song_name, format, link_to_file FROM Lyrics join Song WHERE lyric_id = '" . $safe_lyric_id . "', song_id = '" . $safe_song_id . "' AND Lyrics.song_id = Song.song_id";
}
*/

if($query_string && $safe_lyric_id){
    $query_string .= " AND lyric_id='" . $safe_lyric_id . "'";
}
else if(!$query_string && $safe_lyric_id){
    $query_string .= " lyric_id='" . $safe_lyric_id . "'";
}

if($query_string && $safe_song_id){
    $query_string .= " AND Lyrics.song_id='" . $safe_song_id . "'";
}
else if(!$query_string && $safe_song_id){
    $query_string .= " Lyrics.song_id='" . $safe_song_id . "'";
}

if($query_string && $safe_lyric_format){
    $query_string .= " AND format='" . $safe_lyric_format . "'";
}
else if(!$query_string && $safe_lyric_format){
    $query_string .= " format='" . $safe_lyric_format . "'";
}

if($query_string && $safe_limit){
    $query_string .= " LIMIT " . $safe_limit;
}

$get_lyric_info_query .= $query_string;
//echo "query: " . $get_lyric_info_query . "--------------------";


$get_lyric_info_response = mysqli_query($mysqli, $get_lyric_info_query) or die(mysqli_error($mysqli));


$lyricifo = "[";

while($row = mysqli_fetch_assoc($get_lyric_info_response)){
    $lyricifo .= json_encode($row);
    $lyricifo .= ",";
}
                                                           
$lyricifo = rtrim($lyricifo, ",");
$lyricifo .= "]";
echo $lyricifo;

mysqli_free_result($get_lyric_info_response);
mysqli_close($mysqli);

?>