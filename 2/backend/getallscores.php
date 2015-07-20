<?php
header("Content-Type: application/json");

include 'common.php';
doDB();

//TODO: order by from get...for all the getall* php files

#$get_all_scores_query = "SELECT user_id, song_id, score, achived_date FROM Scores ORDER BY song_id ASC";
$get_all_scores_query = "SELECT Scores.song_id, score, song_name, Scores.user_id, username, achived_date FROM Scores, Users, Song WHERE Scores.song_id = Song.song_id AND Scores.user_id = Users.user_id";
$get_all_scores_response = mysqli_query($mysqli, $get_all_scores_query) or die(mysqli_error($mysqli));

$scoressdata = '[';

while($row = mysqli_fetch_assoc($get_all_scores_response)) {
    $scoressdata .= json_encode($row);
    $scoressdata .=",";
}
mysqli_free_result($get_all_scores_response);
mysqli_close($mysqli);

$scoressdata = rtrim($scoressdata, ",");
$scoressdata .= ']';

echo $scoressdata;

?>