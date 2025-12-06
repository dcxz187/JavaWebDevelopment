// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    console.log("问卷系统已加载");

    // 如果在结果页面，自动获取并显示结果
    if (document.getElementById('userName')) {
        loadAndDisplayResults();
    }
});

// 提交问卷的函数
function submitQuestionnaire() {
    // 获取表单数据
    const name = document.querySelector('input[name="name"]').value;
    const gender = document.querySelector('input[name="gender"]:checked');
    const age = document.querySelector('input[name="age"]').value;
    const department = document.querySelector('select[name="department"]').value;
    const hobbies = document.querySelectorAll('input[name="hobbies"]:checked');
    
    // 基本验证
    if (!name) {
        alert("请输入姓名");
        return false;
    }
    
    if (!gender) {
        alert("请选择性别");
        return false;
    }
    
    if (!age || age <= 0) {
        alert("请输入有效的年龄");
        return false;
    }
    
    if (!department) {
        alert("请选择院系");
        return false;
    }

    return true;
}

// 返回问卷页面的函数
function goBackToQuestionnaire() {
    window.location.href = 'questionnaire';
}

// 获取结果数据的函数
function fetchResults() {
    return fetch('api/results')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                return data.data;
            } else {
                console.error('获取结果失败:', data.message);
                throw new Error(data.message);
            }
        })
        .catch(error => {
            console.error('获取结果时出错:', error);
            throw error;
        });
}

// 显示结果数据的函数
function displayResults(data) {
    const currentResult = data.currentResult;
    
    // 显示用户填写信息
    if (currentResult) {
        document.getElementById('userName').textContent = currentResult.name || '';
        document.getElementById('userGender').textContent = currentResult.gender || '';
        document.getElementById('userAge').textContent = currentResult.age || '';
        document.getElementById('userDepartment').textContent = currentResult.department || '';
        document.getElementById('userHobbies').textContent = data.hobbiesDisplay || '';
    }
    
    // 显示统计信息
    document.getElementById('totalParticipants').textContent = data.totalParticipants || 0;
    document.getElementById('averageAge').textContent = data.averageAge || '0.00';
}

// 加载并显示结果的函数
function loadAndDisplayResults() {
    fetchResults()
        .then(data => {
            displayResults(data);
        })
        .catch(error => {
            console.error('加载结果时出错:', error);
        });
}