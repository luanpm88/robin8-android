$(document).ready(function() {
  var loading = new CreateLoader();
  loading.show();

  $.ajax({
    url: SERVERHOST + 'api/v2_0/e_wallets/unpaid_list',
    type: 'GET',
    success: function(data) {
      loading.destroy();
      var _data = data.list;
      console.log(_data);
      $.each(_data, function(index, el) {
        $('#wallet_unpaid_list').append(createItem(el));
      });
    },
    error: function(xhr, type) {
      loading.destroy();
      console.log('post data error');
    }
  });
});

function createItem(data) {
  var _data = data;
  var status_class = '';
  if (_data.status == '待支付') {
    status_class = 'pending';
  } else if (_data.status == '支付成功') {
    status_class = 'success';
  } else {
    status_class = 'fail';
  }
  var _ui = '<tr>' +
              '<td>' +
                '<p>'+ _data.title +'</p>' +
                '<p class="time">'+ _data.time +'</p>' +
              '</td>' +
              '<td class="amount text-center">'+ _data.amount +'</td>' +
              '<td class="status text-center '+ status_class +'">'+ _data.status +'</td>' +
            '</tr>';

  return _ui;
}
