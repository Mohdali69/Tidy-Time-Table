<?php

require("conn.php");

$tache_nom = $_POST["nom"];
$tache_date_debut = $_POST["date_debut"];
$tache_date_fin = $_POST["date_fin"];
$tache_commentaire = $_POST["commentaire"];

$conn = mysqli_connect(DB_HOST, DB_USERNAME, DB_PASSWORD, DB_NAME);
$sql_query = "insert into `tache` (nom,`date_debut`,date_fin,commentaire) values ('$tache_nom','$tache_date_debut','$tache_date_fin','$tache_commentaire');";

if (mysqli_query($conn, $sql_query)) {
    echo "1";
} else {
    echo "-1";
}
