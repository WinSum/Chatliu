/**
 * 提交问题回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 2, content);
}


function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccept = confirm(response.message);
                    if (isAccept) {
                        window.open("https://github.com/login/oauth/authorize?client_id=9f4a298ad500724a3733&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
                        window.localStorage.setItem("closeable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
            console.log(response)
        },
        dataType: "json"
    });
}

/**
 * 提交评论回复
 * @param commentId
 */
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 1, content)

}


/**
 * 展开二级评论
 */

function collspseComments(e) {

    var id = e.getAttribute("data-id");
    var comment_id = $("#comment-" + id);

    //切换二级评论
    comment_id.toggleClass("in");
    var iconcomment = $("#iconcomment-" + id);
    iconcomment.toggleClass("menucolor")

    var subCommentContainer = $("#comment-" + id);

    //如果显示就追加内容
    if (comment_id.hasClass("in")) {
        //当只有一个标签时，添加。
        if (subCommentContainer.children().length == 1) {
            $.getJSON("/comment/" + id, function (data) {
                console.log(data);
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h6/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "style":"font-size: 13px",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media",
                        "style": "margin-bottom: 10px;border-bottom: 1px solid #eee"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    subCommentContainer.prepend(mediaElement);
                });
            })
        }
    }
}


function showSelectTag() {
    $("#select-tag").show();
}



// 如果标签栏没有这个标签添加
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    if (previous.indexOf(value) == -1) {
        if (previous) {
            $("#tag").val(previous + ',' + value);
        } else {
            $("#tag").val(value);
        }
    }
}