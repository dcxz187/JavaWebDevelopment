document.addEventListener('DOMContentLoaded', function() {
    // 绑定删除按钮点击事件
    const deleteBtn = document.getElementById('deleteBtn');
    if (deleteBtn) {
        deleteBtn.addEventListener('click', handleDeleteProduct);
    }
});

// 处理删除物品
function handleDeleteProduct() {
    if (confirm('确定要删除这个物品吗？')) {
        // 获取物品ID
        const urlParams = new URLSearchParams(window.location.search);
        const productId = urlParams.get('id');
        
        // 发送删除请求
        fetch(getContextPath() + '/deleteProduct?id=' + productId, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('物品删除成功');
                window.location.href = getContextPath() + '/products';
            } else {
                alert('删除失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('删除物品时发生错误，请稍后重试');
        });
    }
}

// 获取应用上下文路径
function getContextPath() {
    return document.querySelector('script[data-context-path]').dataset.contextPath || '';
}