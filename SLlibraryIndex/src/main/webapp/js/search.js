const SEARCH_HISTORY_KEY = 'searchHistory';
const MAX_HISTORY_ITEMS = 10;

// DOM 元素
const searchForm = document.getElementById('searchForm');
const searchInput = document.getElementById('search-input');
const searchResults = document.getElementById('searchResults');
const searchHistoryDatalist = document.getElementById('searchHistory');

// 调试日志：确认脚本加载和 DOM 元素
console.log('search.js 加载成功');
console.log('searchForm:', searchForm);
console.log('searchInput:', searchInput);
console.log('searchResults:', searchResults);
console.log('searchHistoryDatalist:', searchHistoryDatalist);

// 检查 DOM 元素是否存在
if (!searchForm || !searchInput || !searchResults || !searchHistoryDatalist) {
    console.error('DOM 元素缺失：', {
        searchForm: !!searchForm,
        searchInput: !!searchInput,
        searchResults: !!searchResults,
        searchHistoryDatalist: !!searchHistoryDatalist
    });
}

// 初始化函数
document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM 加载完成');
    loadSearchHistory();
});

// 搜索历史相关函数
function loadSearchHistory() {
    const history = JSON.parse(localStorage.getItem(SEARCH_HISTORY_KEY)) || [];
    console.log('加载搜索历史:', history);
    updateSearchHistoryDatalist(history);
}

function saveSearchHistory(keyword) {
    let history = JSON.parse(localStorage.getItem(SEARCH_HISTORY_KEY)) || [];
    history = history.filter(item => item !== keyword);
    history.unshift(keyword);
    if (history.length > MAX_HISTORY_ITEMS) {
        history = history.slice(0, MAX_HISTORY_ITEMS);
    }
    localStorage.setItem(SEARCH_HISTORY_KEY, JSON.stringify(history));
    console.log('保存搜索历史:', history);
    updateSearchHistoryDatalist(history);
}

function updateSearchHistoryDatalist(history) {
    if (!searchHistoryDatalist) {
        console.error('searchHistoryDatalist 未找到');
        return;
    }
    searchHistoryDatalist.innerHTML = '';
    history.forEach(item => {
        const option = document.createElement('option');
        option.value = item;
        searchHistoryDatalist.appendChild(option);
    });
    console.log('更新搜索历史下拉列表:', history);
}

// 结果显示函数
function displayResults(results) {
    console.log('displayResults 调用，results:', results);
    if (!searchResults) {
        console.error('searchResults 元素未找到');
        alert('页面错误：未找到结果显示区域');
        return;
    }
    if (!results || results.length === 0) {
        console.log('显示无结果提示');
        alert('未找到相关结果');
        searchResults.innerHTML = ''; // 清空结果区域
        return;
    }
    const resultsText = results.map(item =>
        `标题: ${item.title || '未知标题'}\n作者: ${item.author || '未知作者'}\n出版年份: ${item.year || '未知年份'}`
    ).join('\n\n');
    alert(`搜索成功\n\n${resultsText}`);
    searchResults.innerHTML = ''; // 清空结果区域
}

// 搜索处理函数
if (searchForm) {
    searchForm.addEventListener('submit', async (event) => {
        console.log('表单提交，keyword:', searchInput.value);
        event.preventDefault();
        if (!searchInput) {
            console.error('searchInput 元素未找到');
            alert('搜索输入框未找到');
            return;
        }
        const keyword = searchInput.value.trim();
        if (!keyword) {
            console.log('关键词为空');
            alert('请输入搜索关键词');
            return;
        }
        try {
            console.log('开始 fetch 请求');
            const response = await fetch(`api/search?keyword=${encodeURIComponent(keyword)}`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });
            console.log('fetch 响应状态:', response.status);
            if (!response.ok) {
                throw new Error(`HTTP错误: ${response.status} ${response.statusText}`);
            }
            const data = await response.json();
            console.log('fetch 数据:', data);
            if (!data || typeof data !== 'object') {
                throw new Error('服务器返回的数据格式无效');
            }
            if (!data.success) {
                throw new Error(data.message || '搜索失败');
            }
            displayResults(data.results);
            saveSearchHistory(keyword);
        } catch (error) {
            console.error('搜索出错:', error);
            alert(`搜索出错: ${error.message}`);
            if (searchResults) {
                searchResults.innerHTML = ''; // 清空结果区域
            } else {
                console.error('无法显示错误，因为 searchResults 未找到');
            }
        }
    });
} else {
    console.error('无法绑定 submit 事件，因为 searchForm 未找到');
    alert('页面错误：未找到搜索表单');
}