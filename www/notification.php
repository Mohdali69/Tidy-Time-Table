<?php

require("conn.php");

$reset = $_POST['reset'];
$count = $_POST['count'];

$conn = mysqli_connect(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);

if ($reset == "reset") {
    $sql_query = "update `user` set badge = 0 where statut = true";
    mysqli_query($conn, $sql_query);
} else if ($count == "count") {
    $sql_query = "select badge from `user` where statut = true";
    $result = mysqli_query($conn, $sql_query);
    while ($row = $result->fetch_assoc()) {
        $nbNotification = $row['badge'];
    }
    echo $nbNotification;
} else {
    $sql_query = "update `user` set badge = badge + 1 where statut = true";
    mysqli_query($conn, $sql_query);

    define( 'API_ACCESS_KEY', 'AAAAm7La89s:APA91bFfLSZgy7nhbXuG9ZSbzE5ZTqDO3vVkcwAmmKf6YTwXBCOh7Us30I8n8DeII5pVaKWgyToprUuu7D4OUWWZmolqjVR-e0b0YM7cldxAlREVPz-mPKCCVFOdB75anGgLaXf6p5ho' );
//$registrationIds = $_GET['id'];

    $msg = array
    (
        'body' 	=> 'Body  Of Notification',
        'title'	=> 'Title Of Notification',
        'icon'	=> 'myicon',/*Default Icon*/
        'sound' => 'mySound'/*Default sound*/
    );

    $fields = array
    (
        'to'		=> '/topics/food',
        'notification'	=> $msg
    );

    $headers = array
    (
        'Authorization: key=' . API_ACCESS_KEY,
        'Content-Type: application/json'
    );

    $ch = curl_init();
    curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
    curl_setopt( $ch,CURLOPT_POST, true );
    curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
    curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
    curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
    curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
    $result = curl_exec($ch );
    curl_close( $ch );

    echo $result;
}

mysqli_close($conn);

