// 显示成功消息
document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const success = urlParams.get('success');
    if (success) {
        document.getElementById('successMessage').textContent = decodeURIComponent(success);
        document.getElementById('successMessage').style.display = 'block';
    }
    
    // 绑定登录表单提交事件
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', handleLoginSubmit);
    }
});

// 处理登录表单提交
function handleLoginSubmit(e) {
    e.preventDefault();
    
    // 隐藏之前的消息
    document.getElementById('errorMessage').style.display = 'none';
    document.getElementById('successMessage').style.display = 'none';
    
    // 获取表单数据
    const formData = new FormData(this);
    
    // 发送登录请求
    fetch(getContextPath() + '/login', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 显示成功消息
            document.getElementById('successMessage').textContent = data.message;
            document.getElementById('successMessage').style.display = 'block';
            
            // 1秒后跳转到首页
            setTimeout(() => {
                window.location.href = getContextPath() + '/products';
            }, 1000);
        } else {
            // 显示错误消息
            document.getElementById('errorMessage').textContent = data.message;
            document.getElementById('errorMessage').style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('errorMessage').textContent = '登录时发生错误，请稍后重试';
        document.getElementById('errorMessage').style.display = 'block';
    });
}

// 获取应用上下文路径
function getContextPath() {
    return document.querySelector('script[data-context-path]').dataset.contextPath || '';
}