document.addEventListener('DOMContentLoaded', function() {
    // 绑定编辑物品表单提交事件
    const editProductForm = document.getElementById('editProductForm');
    if (editProductForm) {
        editProductForm.addEventListener('submit', handleEditProductSubmit);
    }
});

// 处理编辑物品表单提交
function handleEditProductSubmit(e) {
    e.preventDefault();
    
    // 隐藏之前的消息
    document.getElementById('errorMessage').style.display = 'none';
    document.getElementById('successMessage').style.display = 'none';
    
    // 获取表单数据
    const formData = new FormData(this);
    
    // 发送更新物品请求
    fetch(getContextPath() + '/editProduct', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // 显示成功消息
            document.getElementById('successMessage').textContent = data.message;
            document.getElementById('successMessage').style.display = 'block';
            
            // 获取物品ID
            const productId = document.querySelector('input[name="id"]').value;
            
            // 2秒后跳转到物品详情页面
            setTimeout(() => {
                window.location.href = getContextPath() + '/product?id=' + productId;
            }, 2000);
        } else {
            // 显示错误消息
            document.getElementById('errorMessage').textContent = data.message;
            document.getElementById('errorMessage').style.display = 'block';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('errorMessage').textContent = '更新物品时发生错误，请稍后重试';
        document.getElementById('errorMessage').style.display = 'block';
    });
}

// 获取应用上下文路径
function getContextPath() {
    return document.querySelector('script[data-context-path]').dataset.contextPath || '';
}