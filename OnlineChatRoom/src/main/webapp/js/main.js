// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    console.log("聊天室系统已加载");
});

// 获取消息数据的函数
function fetchMessages() {
    return fetch('api/messages')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                return data.data;
            } else {
                console.error('获取消息失败:', data.message);
                throw new Error(data.message);
            }
        })
        .catch(error => {
            console.error('获取消息时出错:', error);
            throw error;
        });
}

// 显示消息数据的函数
function displayMessages(data) {
    const messagesContainer = document.getElementById('messagesContainer');
    const onlineUsersList = document.getElementById('onlineUsersList');
    const currentUsernameElement = document.getElementById('currentUsername');
    
    if (!messagesContainer || !onlineUsersList) {
        return;
    }
    
    // 显示当前用户
    if (currentUsernameElement) {
        currentUsernameElement.textContent = data.currentUser;
    }
    
    // 显示在线用户
    onlineUsersList.innerHTML = '';
    data.onlineUsers.forEach(username => {
        const li = document.createElement('li');
        li.textContent = username;
        if (username === data.currentUser) {
            li.innerHTML += ' (你)';
        } else {
            // 添加私聊链接
            li.innerHTML += ` <a href="private-chat?user=${encodeURIComponent(username)}" style="font-size: 0.8em;">[私聊]</a>`;
        }
        onlineUsersList.appendChild(li);
    });
    
    // 显示消息
    messagesContainer.innerHTML = '';
    data.messages.forEach(message => {
        const messageDiv = document.createElement('div');
        
        // 根据消息类型设置样式
        if (message.type === "system") {
            messageDiv.className = 'system-message';
            messageDiv.innerHTML = `<strong>${escapeHtml(message.content)}</strong>`;
        } else {
            messageDiv.className = 'message';
            
            const isOwnMessage = message.username === data.currentUser;
            
            messageDiv.innerHTML = `
                <div class="username">${message.username}${isOwnMessage ? ' (你)' : ''}</div>
                <div class="timestamp">${message.timestamp}</div>
                <div class="content">${escapeHtml(message.content)}</div>
            `;
        }
        
        messagesContainer.appendChild(messageDiv);
    });
    
    // 滚动到最新消息
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

// 加载并显示消息的函数
function loadAndDisplayMessages() {
    fetchMessages()
        .then(data => {
            displayMessages(data);
        })
        .catch(error => {
            console.error('加载消息时出错:', error);
            // 如果是未登录错误，重定向到登录页面
            if (error.message && error.message.includes('未登录')) {
                window.location.href = 'login';
            }
        });
}

// 发送消息的函数
function sendMessage() {
    const messageInput = document.getElementById('messageInput');
    const content = messageInput.value.trim();
    
    if (!content) {
        return;
    }
    
    // 创建URL编码的表单数据
    const params = new URLSearchParams();
    params.append('content', content);
    
    // 发送消息
    fetch('api/send', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        },
        body: params
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 清空输入框
            messageInput.value = '';
            // 重新加载消息
            loadAndDisplayMessages();
        } else {
            console.error('发送消息失败:', data.message);
            alert('发送消息失败: ' + data.message);
        }
    })
    .catch(error => {
        console.error('发送消息时出错:', error);
        alert('发送消息时出错');
    });
}

// 处理键盘事件
function handleKeyDown(event) {
    if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault();
        sendMessage();
    }
}

// 退出登录
function logout() {
    if (confirm('确定要退出聊天室吗？')) {
        // 清除会话并重定向到登录页面
        fetch('logout', {
            method: 'POST'
        }).finally(() => {
            window.location.href = 'login';
        });
    }
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
    
    return text.replace(/[&<>"']/g, function(m) { return map[m]; });
}

// 心跳检测函数
function sendHeartbeat() {
    fetch('api/heartbeat')
        .then(response => response.json())
        .then(data => {
            if (!data.success) {
                // 心跳失败，可能是会话过期
                console.warn('Heartbeat failed:', data.message);
                if (data.message && data.message.includes('未登录')) {
                    window.location.href = 'login';
                }
            }
        })
        .catch(error => {
            console.error('Heartbeat error:', error);
        });
}

// 开始心跳检测
function startHeartbeat() {
    // 每30秒发送一次心跳
    setInterval(sendHeartbeat, 30000);
    // 立即发送第一次心跳
    sendHeartbeat();
}

// 在用户关闭页面前发送退出请求
window.addEventListener('beforeunload', function(e) {
    navigator.sendBeacon('logout', '');
});