const backendLocation = 'http://localhost:8080'

let productId = document.getElementById("productId").getAttribute("value")
let commentSection = document.getElementById("commentCtnr")

const csrfHeaderName = document.head.querySelector('[name=_csrf_header]').content
const csrfHeaderValue = document.head.querySelector('[name=_csrf]').content


fetch(`${backendLocation}/api/${productId}/comments`, {
    method: 'GET',
    headers: {
        "Accept": "application/json"
    }
}).then((response) => response.json())
    .then((body) => {
        for(comment of body) {
            addCommentAsHtml(comment)
        }
    })

function addCommentAsHtml(comment) {
    let commentHtml = `<div class="comments" id="comment${comment.id}">\n`
    commentHtml += '<div hidden>' + comment.id + '</div>'
    commentHtml += '<h4>' + comment.authorName + '</h4>\n'
    commentHtml += '<p>' + comment.text + '</p>\n'
    commentHtml += '<span>' + comment.dateOfCreation + '</span>\n'

    if(comment.canEdit) {
        commentHtml += `<button class="btn btn-danger" onclick="deleteComment(${comment.id})">Delete</button>\n`
    }

    commentHtml += '</div>\n'

    commentSection.innerHTML += commentHtml
}

function deleteComment(commentId) {
    fetch(`${backendLocation}/api/${productId}/comments/${commentId}`, {
        method: 'DELETE',
        headers: {
            [csrfHeaderName]: csrfHeaderValue
        }
    }).then(res => {
        console.log(res)
        document.getElementById("comment" + commentId).remove()
    })
}


let commentForm = document.getElementById("commentForm")
commentForm.addEventListener("submit", (event) => {
    event.preventDefault()

    let text = document.getElementById("message").value

    fetch(`${backendLocation}/api/${productId}/comments`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfHeaderValue
        },
        body: JSON.stringify({
            text: text
        })
    }).then((res) => {
        document.getElementById("message").value = ""
        let location = res.headers.get("Location")
        fetch(`${backendLocation}${location}`)
            .then(res => res.json())
            .then(body => addCommentAsHtml(body))
            .then(x => window.location.href=`/products/details/${productId}`)
    })
})