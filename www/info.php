<?php

require("conn.php");

$current_user = $_POST['current_user'];

$conn = mysqli_connect(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
$sql_query = "select id,lastname,`name` from `user` where email like '$current_user';";
$result = mysqli_query($conn,$sql_query);

while ($row = $result->fetch_assoc()) {
    $id = $row['id'];
    $lastname = $row['lastname'];
    $name = $row['name'];
}

echo $id."/".$lastname."~".$name;

mysqli_close($conn);

