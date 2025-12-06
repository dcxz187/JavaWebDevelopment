// 商品列表页面的JavaScript代码
function loadProducts() {
    // 发送请求到后端获取商品数据
    fetch('api/products')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                displayProducts(data.data);
            } else {
                console.error('加载商品失败:', data.message);
            }
        })
        .catch(error => {
            console.error('加载商品时出错:', error);
        });
}

// 显示商品列表
function displayProducts(products) {
    const productList = document.getElementById('productList');
    
    if (!products || products.length === 0) {
        productList.innerHTML = '<p>暂无商品</p>';
        return;
    }
    
    let html = '';
    products.forEach(product => {
        html += `
            <div class="product-item">
                <h3>${product.name}</h3>
                <p>${product.description}</p>
                <p class="price">价格：￥${product.price.toFixed(2)}</p>
                <form action="cart" method="post">
                    <input type="hidden" name="productId" value="${product.id}">
                    <label>
                        数量：<input type="number" name="quantity" value="1" min="1" max="99">
                    </label>
                    <input type="submit" value="添加到购物车">
                </form>
            </div>
        `;
    });
    
    productList.innerHTML = html;
}

// 加载购物车数据
function loadCart() {
    // 发送请求到后端获取购物车数据
    fetch('api/cart')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                displayCart(data.data);
            } else {
                console.error('加载购物车失败:', data.message);
            }
        })
        .catch(error => {
            console.error('加载购物车时出错:', error);
        });
}

// 显示购物车内容
function displayCart(cartItems) {
    const cartItemsElement = document.getElementById('cartItems');
    const totalAmountElement = document.getElementById('totalAmount');
    
    if (!cartItems || cartItems.length === 0) {
        cartItemsElement.innerHTML = '<tr><td colspan="4">购物车为空</td></tr>';
        totalAmountElement.textContent = '￥0.00';
        return;
    }
    
    let html = '';
    let total = 0;
    
    cartItems.forEach(item => {
        const itemTotal = item.product.price * item.quantity;
        total += itemTotal;
        
        html += `
            <tr>
                <td data-label="商品名称">${item.product.name}</td>
                <td data-label="单价">￥${item.product.price.toFixed(2)}</td>
                <td data-label="数量">${item.quantity}</td>
                <td data-label="小计">￥${itemTotal.toFixed(2)}</td>
            </tr>
        `;
    });
    
    cartItemsElement.innerHTML = html;
    totalAmountElement.textContent = '￥' + total.toFixed(2);
}