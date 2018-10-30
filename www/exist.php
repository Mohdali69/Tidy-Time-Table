<?php

$id = $_POST['id'];

if(file_exists("images/" . $id . ".png")) {
    echo "1";
} else {
    echo "-1";
}
