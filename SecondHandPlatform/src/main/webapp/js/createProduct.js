document.addEventListener('DOMContentLoaded', function() {
    // 绑定发布物品表单提交事件
    const createProductForm = document.getElementById('createProductForm');
    if (createProductForm) {
        createProductForm.addEventListener('submit', handleCreateProductSubmit);
    }
});

// 处理发布物品表单提交
function handleCreateProductSubmit(e) {
    e.preventDefault();
    
    // 隐藏之前的消息
    document.getElementById('errorMessage').style.display = 'none';
    document.getElementById('successMessage').style.display = 'none';
    
    // 获取表单数据
    const formData = new FormData(this);
    
    // 发送发布物品请求
    fetch(getContextPath() + '/createProduct', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 显示成功消息
            document.getElementById('successMessage').textContent = data.message;
            document.getElementById('successMessage').style.display = 'block';
            
            // 清空表单
            document.getElementById('createProductForm').reset();
            
            // 2秒后跳转到首页
            setTimeout(() => {
                window.location.href = getContextPath() + '/products';
            }, 2000);
        } else {
            // 显示错误消息
            document.getElementById('errorMessage').textContent = data.message;
            document.getElementById('errorMessage').style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('errorMessage').textContent = '发布物品时发生错误，请稍后重试';
        document.getElementById('errorMessage').style.display = 'block';
    });
}

// 获取应用上下文路径
function getContextPath() {
    return document.querySelector('script[data-context-path]').dataset.contextPath || '';
}