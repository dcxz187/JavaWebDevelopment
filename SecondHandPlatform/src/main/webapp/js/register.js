document.addEventListener('DOMContentLoaded', function() {
    // 绑定注册表单提交事件
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegisterSubmit);
    }
});

// 处理注册表单提交
function handleRegisterSubmit(e) {
    e.preventDefault();
    
    // 隐藏之前的消息
    document.getElementById('errorMessage').style.display = 'none';
    document.getElementById('successMessage').style.display = 'none';
    
    // 获取表单数据
    const formData = new FormData(this);
    
    // 发送注册请求
    fetch(getContextPath() + '/register', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 显示成功消息
            document.getElementById('successMessage').textContent = data.message;
            document.getElementById('successMessage').style.display = 'block';
            
            // 3秒后跳转到登录页面
            setTimeout(() => {
                window.location.href = getContextPath() + '/login';
            }, 3000);
        } else {
            // 显示错误消息
            document.getElementById('errorMessage').textContent = data.message;
            document.getElementById('errorMessage').style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('errorMessage').textContent = '注册时发生错误，请稍后重试';
        document.getElementById('errorMessage').style.display = 'block';
    });
}

// 获取应用上下文路径
function getContextPath() {
    return document.querySelector('script[data-context-path]').dataset.contextPath || '';
}