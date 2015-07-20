<?php
/*
    Gets
        song info - song id, name, artist, album, genre, length, diff, artwork
        media info - media id, type, link
        lyric info - format, link_to_file
    
    takes in
        none - get all 
        name - searches with LIKE
        difficulty - slow, medium, hard
        orderby - app uses title name and artist
        asc or desc
*/

header("Content-Type: application/json; charset=utf-8");

include 'common.php';

doDB();

$safe_name = "";
$safe_artist  = "";
$safe_diff = "";
$safe_orderby = "";
$safe_asc = "";
$safe_limit = "";

if(isset($_GET['name'])) {
    $safe_name = mysqli_real_escape_string($mysqli, $_GET['name']);
}
if(isset($_GET['artist'])) {
    $safe_artist = mysqli_real_escape_string($mysqli, $_GET['artist']);
}
if(isset($_GET['diff'])) {
    $safe_diff = mysqli_real_escape_string($mysqli, $_GET['diff']);
}
if(isset($_GET['orderby'])) {
    $safe_orderby = mysqli_real_escape_string($mysqli, $_GET['orderby']);
}
if(isset($_GET['asc'])) {
    $safe_asc = mysqli_real_escape_string($mysqli, $_GET['asc']);
}
if(isset($_GET['limit'])) {
    $safe_limit = mysqli_real_escape_string($mysqli, $_GET['limit']);
}

if(!$safe_name && !$safe_artist && !$safe_diff && !$safe_orderby && !$safe_asc && $safe_limit) {
    echo '[{error: "you\' not searching for anything"}]';
    exit();
}

$songselect_query = <<<QUERY
    SELECT 
    
    Song.song_id, Song.song_name, Song.song_artist, Song.song_album, Song.song_genre, Song.song_length, Song.song_diff, Song.album_artwork,
    
    Lyrics.lyric_id, Lyrics.format, Lyrics.link_to_file,
    
    Media.media_id, Media.type_of_link, Media.link
    
    FROM Song join Media on Song.song_id = Media.song_id join Lyrics on Song.song_id = Lyrics.song_id
    
QUERY;

if($safe_name || $safe_artist || $safe_diff || $safe_orderby || $safe_asc) {
    $songselect_query .= "WHERE ";
    
    $query_string = "";
    
    if($query_string && $safe_name){
        $query_string .= " AND Song.song_name LIKE '%". $safe_name ."%'";
    }
    else if(!$query_string && $safe_name){
        $query_string .= " Song.song_name LIKE '%". $safe_name ."%'";
    }
    
    if($query_string && $safe_artist){
        $query_string .= " AND Song.song_artist LIKE '%". $safe_artist ."%'";
    }
    else if(!$query_string && $safe_artist){
        $query_string .= " Song.song_artist LIKE '%". $safe_artist ."%'";
    }
    
    if($query_string && $safe_diff){
        $query_string .= " AND Song.song_diff = '". $safe_diff ."'";
    }
    else if(!$query_string && $safe_diff){
        $query_string .= " Song.song_diff = '". $safe_diff ."'";
    }
    
    if($safe_orderby){
        $query_string .= " ORDER BY ". $safe_orderby;
    }
    
    /*
    if($safe_orderby && $safe_asc){
        $query_string .= " AND ";
    }
    else if(!$query_string && $safe_asc){
        $query_string .= " ";
    }*/
    
    if($safe_limit){
        $query_string .= " LIMIT ". $safe_limit;
    }
    
    $songselect_query .= $query_string;
}


//echo $songselect_query . "-----------------------------------------------------------------------";


$songselect_response = mysqli_query($mysqli, $songselect_query) or die(mysqli_error($mysqli));

$select_song_info = "[";

while($row = mysqli_fetch_assoc($songselect_response)){
    $select_song_info .= json_encode($row);
    $select_song_info .= "," ;
}
                                                           
$select_song_info = rtrim($select_song_info, ",");
$select_song_info .= "]";
echo $select_song_info;

mysqli_free_result($songselect_response);
mysqli_close($mysqli);


?>