  const form = document.getElementById('search-form');
  const queryInput = document.getElementById('query-input');

  form.addEventListener('submit', function (event) {
  if (queryInput.value.trim().length < 2) {
  event.preventDefault(); // 검색을 실행하지 않음
  alert('검색어를 두글자 이상 입력해주세요.');
} else if (event.keyCode === 13 && queryInput.value.trim().length > 30){
  event.preventDefault(); // 검색을 실행하지 않음
  alert('검색어를 30글자 이하로 입력해주세요.');
}
});

  queryInput.addEventListener('keypress', function (event) {
  if (event.keyCode === 13 && queryInput.value.trim().length < 2) {
  event.preventDefault(); // 검색을 실행하지 않음
  alert('검색어를 2글자 이상 입력해주세요.');
} else if (event.keyCode === 13 && queryInput.value.trim().length > 30){
  event.preventDefault(); // 검색을 실행하지 않음
  alert('검색어를 30글자 이하로 입력해주세요.');
}
});

  const query = new URLSearchParams(window.location.search).get('query');
  document.getElementById('query').textContent = query;

  let copyBtns = document.querySelectorAll('.copy-btn');
  let alertBox = document.querySelector('.alert');
  copyBtns.forEach(function (btn) {
  btn.addEventListener('click', function (event) {
    // 이벤트 버블링 방지
    event.stopPropagation();

    // 클립보드에 복사할 텍스트 가져오기
    let text = event.target.dataset.title;

    // 클립보드에 복사
    navigator.clipboard.writeText(text)
    .then(function () {
      console.log('제목이 클립보드에 복사되었습니다.');
      // 알람 띄우기
      alertBox.style.display = 'block';
      setTimeout(function () {
        alertBox.style.display = 'none';
      }, 500); // 0.5초 후 알람 닫기
    })
    .catch(function (err) {
      console.error('클립보드 복사에 실패하였습니다.', err);
    });
  });
});

  function openPopup() {
  const areaCd = document.getElementById('search-input').value;
  const url = `/library/mapSearch?areaCd=${areaCd}`;
  window.open(url, 'areaCdInfo', 'width=1200,height=800');
  return false;
}
  /*<![CDATA[*/
  let searchResultData = /*[[${searchResult}]]*/ [];
  /*]]>*/

  $(document).ready(function() {
  let urlParams = new URLSearchParams(window.location.search);
  let startPage;
  let endPage;

  let query = urlParams.get('query');
  let currentPage = parseInt(urlParams.get('page') || 1);

  // 페이지 링크 생성 및 할당
  let totalPages = 5;
  const url = "/books/search?query=" + query + "&page=";

  function generatePageLinks(currentPage) {
  let pages = $(".pages");
  pages.empty();
  // 페이지 링크 생성
  totalPages = currentPage - 1 + 5
  let startPage = Math.floor((currentPage - 1) / 5) * 5 + 1;
  let endPage = startPage + 4;
  if (endPage > totalPages) {
  endPage = totalPages;
}
  for (let i = startPage; i <= endPage; i++) {
  let pageLink = $("<a>").attr("href", url + i).addClass("page-link").text(i);
  let pageItem = $("<li>").addClass("page-item").append(pageLink);
  if (i === currentPage || (currentPage >= startPage && currentPage <= endPage && i === currentPage)) {
  pageItem.addClass("current");
}
  pages.append(pageItem);
}
}
  generatePageLinks(currentPage);

  // 이전 페이지 버튼 클릭 시 이벤트 처리
  $("#prevBtn").on("click", function() {
  if (currentPage > 1) {
  window.location.href = url + (currentPage - 1);
}
});

  // 다음 페이지 버튼 클릭 시 이벤트 처리
  $("#nextBtn").on("click", function() {
  const nextPage = currentPage + 1;
  if (nextPage <= totalPages) {
  window.location.href = url + (currentPage + 1);
} else {
  currentPage = nextPage; // 현재 페이지를 다음 페이지로 업데이트
  generatePageLinks(nextPage); // 페이지 링크 다시 생성
  window.location.href = url + nextPage;
}
});

  const pageLinks = $(".page-link");
  pageLinks.each(function(index) {
  const pageItem = $(this).parent();
  const pageNum = parseInt($(this).text());
  if (pageNum === currentPage) {
  pageItem.addClass("current");
} else {
  pageItem.removeClass("current");
}
  if (pageNum < startPage || pageNum > endPage) {
  pageItem.hide();
} else {
  pageItem.show();
}
});
});

  $('.loan-check-btn').on('click', function() {
  let isbn = $(this).attr('data-isbn');
  let bigNumberIsbn = new BigNumber(isbn);
  isbn = bigNumberIsbn.toFixed();
  console.log(isbn);
});
  document.addEventListener("DOMContentLoaded", function () {
  const loanCheckButtons = document.querySelectorAll(".loan-check-btn");

  loanCheckButtons.forEach((button) => {
  button.addEventListener("click", function (event) {
  navigator.geolocation.getCurrentPosition(function (position) {
  var lat = position.coords.latitude, // 위도
  lon = position.coords.longitude; // 경도

  const isbn2 = event.target.getAttribute("data-isbn");
  const isbn = BigInt(Number(isbn2));
  openPopup_MapBook(isbn.toString(), lat, lon);

});
});
});
});


  function openPopup_MapBook(isbn, lat, lon) {

  // 데이터 객체에 isbn, lat, lon 값을 추가
  const data = {
  isbn: isbn,
  lat: lat,
  lon: lon
};

  $.ajax({
  type: 'POST',
  url: `/books/mapBook/search`,
  contentType: "application/json",
  data: JSON.stringify(data), // 수정된 data 객체를 JSON 형태로 변환하여 전송
  success: function (response) {
  // 새로운 팝업 창을 열고 응답을 받은 HTML로 내용을 채움
  const popupWindow = window.open('', 'areaCdInfo', 'width=1200,height=800');
  popupWindow.document.write(response);
  popupWindow.document.close();
},
  error(error) {
  console.error(error);
}
});

  return false;
}

  /*--------------------------------------------*/

  /*function addHTML(itemDto) {
  /!**
  * class="search-itemDto" 인 녀석에서
  * image, title, lprice, addProduct 활용하기
  * 참고) onclick='addProduct(${JSON.stringify(itemDto)})'
  *!/
  return `<div class="search-itemDto">
        <div class="search-itemDto-left">
            <img src="${itemDto.image}" alt="">
        </div>
        <div class="search-itemDto-center">
            <div>${itemDto.title}</div>
            <div class="price">
                ${numberWithCommas(itemDto.lprice)}
                <span class="unit">원</span>
            </div>
        </div>
        <div class="search-itemDto-right">
            <img src="../images/icon-save.png" alt="" onclick='addProduct(${JSON.stringify(itemDto)})'>
        </div>
    </div>`
}*/

  function getRecommend() {
  // 현재 페이지 URL에서 'query' 파라미터 값 가져오기
  const urlParams = new URLSearchParams(window.location.search);
  const query = urlParams.get('query');

  if ($('#recommend-box').css('display') === 'none') {
  $('#recommend-box').show();
}

  $.ajax({
  type: 'POST',
  url: `/books/recommend`,
  contentType: "application/json",
  data: JSON.stringify({query}), // query 파라미터를 data 객체에 추가
  success: function (response) {
  $('#recommend-box').empty();
  for (let i = 0; i < response.length; i++) {
  let bookNm = response[i];
  $('#recommend-box').append(`(${i+1}) ${bookNm}  `);
}
  console.log(response);
},
  error(error) {
  console.error(error);
}
})
}