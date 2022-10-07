function sendMsgApi(data) {
    return $axios({
        'url': '/user/sendMsg',
        'methor': 'post',
        data
    })
}