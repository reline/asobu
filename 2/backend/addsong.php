<?php
header("Content-Type: text/html; charset=utf-8");
include 'common.php';

if($_POST) {
    if(!$_POST['input_add_song_name'] || !$_POST['input_add_song_difficulty']) {
        echo "Check required fields: adding songs";
        exit();
    }
    
    doDB();
    
    $safe_name = mysqli_real_escape_string($mysqli, $_POST['input_add_song_name']);
    $safe_artist = mysqli_real_escape_string($mysqli, $_POST['input_add_song_artist']);
    $safe_album = mysqli_real_escape_string($mysqli, $_POST['input_add_song_album']);
    $safe_genre = mysqli_real_escape_string($mysqli, $_POST['input_add_song_genre']);
    $safe_length = mysqli_real_escape_string($mysqli, $_POST['input_add_song_length']);
    $safe_diff = mysqli_real_escape_string($mysqli, $_POST['input_add_song_difficulty']);
    $safe_art = mysqli_real_escape_string($mysqli, $_POST['input_add_song_art']);
    
    //insert into db
    $add_songs_query = "INSERT INTO Song (song_name, song_artist, song_album, song_genre, song_length, song_diff, album_artwork) VALUES ('".$safe_name."', '".$safe_artist."', '".$safe_album."', '".$safe_genre."', '".$safe_length."', '".$safe_diff."', '".$safe_art."')";
    $add_songs_response = mysqli_query($mysqli, $add_songs_query) or die(mysqli_error($mysqli));
    
    //get user id
    $song_id = mysqli_insert_id($mysqli);
    echo "song $song_id added";
    
    mysqli_free_result($add_songs_response);
    mysqli_close($mysqli);
    
}

?>