<?php

$upload_path = 'images/';
$upload_url = 'http://localhost:8888/'.$upload_path;
$current_user = $_POST['current_user'];

$file_info = pathinfo($_FILES['image']['name']);
$extension = $file_info['extension'];
$file_path = $upload_path . $current_user . '.' . $extension;
$file_url = $upload_url . $current_user . '.' . $extension;

move_uploaded_file($_FILES['image']['tmp_name'],$file_path);
