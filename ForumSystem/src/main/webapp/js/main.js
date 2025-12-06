// 检查登录状态
function checkLoginStatus() {
    fetch('api/user')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 更新页面上的用户名
                const usernameElements = document.querySelectorAll('#username');
                usernameElements.forEach(element => {
                    element.textContent = data.data.username;
                });
            } else {
                // 未登录，重定向到登录页面
                window.location.href = 'login.jsp?error=' + encodeURIComponent('请先登录');
            }
        })
        .catch(error => {
            console.error('检查登录状态时出错:', error);
            window.location.href = 'login.jsp?error=' + encodeURIComponent('请先登录');
        });
}

// 登录功能
function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const captcha = document.getElementById('captcha').value;
    
    console.log('登录信息:', { username, password, captcha });
    
    // 检查参数是否完整
    if (!username || !password || !captcha) {
        document.getElementById('errorMessage').textContent = '请填写所有必填字段';
        return;
    }
    
    // 使用URLSearchParams代替FormData
    const params = new URLSearchParams();
    params.append('username', username);
    params.append('password', password);
    params.append('captcha', captcha);
    
    console.log('发送的数据:', params.toString());
    
    // 发送登录请求
    fetch('api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(data => {
        console.log('服务器响应:', data);
        if (data.success) {
            // 登录成功，重定向到主页
            window.location.href = 'home.jsp';
        } else {
            // 登录失败，显示错误消息
            document.getElementById('errorMessage').textContent = data.message;
            // 刷新验证码
            document.getElementById('captchaImg').src = 'api/captcha?' + Math.random();
        }
    })
    .catch(error => {
        console.error('登录时出错:', error);
        document.getElementById('errorMessage').textContent = '登录时发生错误';
    });
}

// 注册功能
function register() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const captcha = document.getElementById('captcha').value;
    
    console.log('注册信息:', { username, password, confirmPassword, captcha });
    
    // 检查参数是否完整
    if (!username || !password || !confirmPassword || !captcha) {
        document.getElementById('errorMessage').textContent = '请填写所有必填字段';
        return;
    }
    
    // 检查两次输入的密码是否一致
    if (password !== confirmPassword) {
        document.getElementById('errorMessage').textContent = '两次输入的密码不一致';
        return;
    }
    
    // 使用URLSearchParams代替FormData
    const params = new URLSearchParams();
    params.append('username', username);
    params.append('password', password);
    params.append('confirmPassword', confirmPassword);
    params.append('captcha', captcha);
    
    console.log('发送的数据:', params.toString());
    
    // 发送注册请求
    fetch('api/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(data => {
        console.log('服务器响应:', data);
        if (data.success) {
            // 注册成功，重定向到登录页面
            window.location.href = 'login.jsp?success=' + encodeURIComponent('注册成功，请登录');
        } else {
            // 注册失败，显示错误消息
            document.getElementById('errorMessage').textContent = data.message;
            // 刷新验证码
            document.getElementById('captchaImg').src = 'api/captcha?' + Math.random();
        }
    })
    .catch(error => {
        console.error('注册时出错:', error);
        document.getElementById('errorMessage').textContent = '注册时发生错误';
    });
}

// 退出登录
function logout() {
    if (confirm('确定要退出登录吗？')) {
        fetch('api/logout', {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // 退出成功，重定向到登录页面
                window.location.href = 'login.jsp';
            } else {
                // 退出失败，显示错误消息
                alert('退出登录失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('退出登录时出错:', error);
            alert('退出登录时发生错误');
        });
    }
}

// 加载帖子列表
function loadThreads() {
    fetch('api/threads')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                displayThreads(data.data);
            } else {
                document.getElementById('threadsList').innerHTML = 
                    '<div class="empty-message">加载帖子失败: ' + data.message + '</div>';
            }
        })
        .catch(error => {
            console.error('加载帖子时出错:', error);
            document.getElementById('threadsList').innerHTML = 
                '<div class="empty-message">加载帖子时发生错误</div>';
        });
}

// 显示帖子列表
function displayThreads(threads) {
    const threadsListElement = document.getElementById('threadsList');
    
    if (!threads || threads.length === 0) {
        threadsListElement.innerHTML = 
            '<div class="empty-message">暂无帖子，<a href="createThread.jsp">创建第一个帖子</a></div>';
        return;
    }
    
    let html = '<ul class="thread-list">';
    
    threads.forEach(thread => {
        html += `
            <li class="thread-item">
                <div class="thread-title">
                    <a href="thread.jsp?id=${thread.id}">${escapeHtml(thread.title)}</a>
                </div>
                <div class="thread-meta">
                    作者: ${escapeHtml(thread.author)} | 
                    发布时间: ${formatDate(thread.createdAt)}
                </div>
                <div class="thread-content">
                    ${escapeHtml(thread.content)}
                </div>
            </li>
        `;
    });
    
    html += '</ul>';
    threadsListElement.innerHTML = html;
}

// 加载帖子详情
function loadThread(threadId) {
    fetch(`api/thread?id=${threadId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                displayThread(data.data.thread);
                displayReplies(data.data.replies);
                // 更新回复数量
                document.getElementById('replyCount').textContent = data.data.replies.length;
            } else {
                document.getElementById('threadContainer').innerHTML = 
                    '<div class="empty-message">加载帖子失败: ' + data.message + '</div>';
            }
        })
        .catch(error => {
            console.error('加载帖子时出错:', error);
            document.getElementById('threadContainer').innerHTML = 
                '<div class="empty-message">加载帖子时发生错误</div>';
        });
}

// 显示帖子详情
function displayThread(thread) {
    const threadContainerElement = document.getElementById('threadContainer');
    
    const html = `
        <div class="thread-container">
            <div class="thread-header-detail">
                <div class="thread-title-detail">${escapeHtml(thread.title)}</div>
                <div class="thread-meta-detail">
                    作者: ${escapeHtml(thread.author)} | 
                    发布时间: ${formatDate(thread.createdAt)}
                </div>
            </div>
            <div class="thread-content-detail">
                ${escapeHtml(thread.content)}
            </div>
        </div>
    `;
    
    threadContainerElement.innerHTML = html;
}

// 显示回复列表
function displayReplies(replies) {
    const repliesListElement = document.getElementById('repliesList');
    
    if (!replies || replies.length === 0) {
        repliesListElement.innerHTML = 
            '<div class="empty-replies">暂无回复，快来发表第一个回复吧！</div>';
        return;
    }
    
    let html = '<ul class="reply-list">';
    
    replies.forEach(reply => {
        html += `
            <li class="reply-item">
                <div class="reply-meta">
                    ${escapeHtml(reply.author)} | 
                    回复时间: ${formatDate(reply.createdAt)}
                </div>
                <div class="reply-content">
                    ${escapeHtml(reply.content)}
                </div>
            </li>
        `;
    });
    
    html += '</ul>';
    repliesListElement.innerHTML = html;
}

// 创建帖子
function createThread() {
    const title = document.getElementById('title').value;
    const content = document.getElementById('content').value;
    
    // 检查参数是否完整
    if (!title || !content) {
        document.getElementById('errorMessage').textContent = '请填写所有必填字段';
        return;
    }
    
    // 使用URLSearchParams代替FormData
    const params = new URLSearchParams();
    params.append('title', title);
    params.append('content', content);
    
    // 发送创建帖子请求
    fetch('api/createThread', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 创建成功，重定向到主页
            window.location.href = 'home.jsp';
        } else {
            // 创建失败，显示错误消息
            document.getElementById('errorMessage').textContent = data.message;
        }
    })
    .catch(error => {
        console.error('创建帖子时出错:', error);
        document.getElementById('errorMessage').textContent = '创建帖子时发生错误';
    });
}

// 添加回复
function addReply(threadId) {
    const content = document.getElementById('content').value;
    
    // 检查参数是否完整
    if (!content) {
        document.getElementById('errorMessage').textContent = '请填写回复内容';
        return;
    }
    
    // 使用URLSearchParams代替FormData
    const params = new URLSearchParams();
    params.append('threadId', threadId);
    params.append('content', content);
    
    // 发送添加回复请求
    fetch('api/addReply', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 回复成功，重新加载帖子详情
            loadThread(threadId);
            // 清空回复内容
            document.getElementById('content').value = '';
        } else {
            // 回复失败，显示错误消息
            document.getElementById('errorMessage').textContent = data.message;
        }
    })
    .catch(error => {
        console.error('添加回复时出错:', error);
        document.getElementById('errorMessage').textContent = '添加回复时发生错误';
    });
}

// 格式化日期
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.getFullYear() + '-' + 
           String(date.getMonth() + 1).padStart(2, '0') + '-' + 
           String(date.getDate()).padStart(2, '0') + ' ' + 
           String(date.getHours()).padStart(2, '0') + ':' + 
           String(date.getMinutes()).padStart(2, '0') + ':' + 
           String(date.getSeconds()).padStart(2, '0');
}

// 转义HTML特殊字符以防止XSS攻击
function escapeHtml(text) {
    const map = {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#039;'
    };
    
    return String(text).replace(/[&<>"']/g, function(m) { return map[m]; });
}