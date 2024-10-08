<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="/playlist/create" method="POST">
		<input type="text" name="playlistName"
			placeholder="Enter Playlist Name">
		<button type="submit">Create Playlist</button>
	</form>

	<table id="musicList">
		<c:forEach var="music" items="${musicList}">
			<tr id="${music.musicId}">
				<td>${music.musicName}</td>
				<td><button type="button"
						onclick="removeMusic(${music.musicId})">Remove</button></td>
			</tr>
		</c:forEach>
	</table>

	<script>
$(function() {
    // Drag-and-drop 기능으로 음악 순서 변경
    $("#musicList").sortable({
        update: function(event, ui) {
            var musicOrder = $(this).sortable("toArray");
            $.post("/playlist/updateOrder", { playlistId: $("#playlistId").val(), musicOrder: musicOrder });
        }
    });

    // 음악 삭제 기능
    function removeMusic(musicId) {
        $.post("/playlist/removeMusic", { playlistId: $("#playlistId").val(), musicId: musicId }, function(response) {
            if (response === "success") {
                $("#" + musicId).remove();
            }
        });
    }
});

</script>

</body>
</html>