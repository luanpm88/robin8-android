$(document).ready(function() {
  // var loading = new CreateLoader();
  var current_token = '';
  var data_params = {};
  var dropLoadCtrl;

  if (typeof jwPut != 'undefined') {
    var native_data = jwPut.current_token_data();
    if (native_data != '') {
      current_token = native_data;
    }
  }
  // loading.show();

  data_params.page = 0;
  data_params.per_page = 10;
  dropLoadCtrl = new DropLoadCtrl(
    '#record_list',
    '.record-list',
    SERVERHOST + 'api/v2_0/e_wallets/unpaid_list',
    data_params,
    current_token,
    recordItem,
    ''
  );
});

function recordItem(data) {
  var _data = data;
  var status_class = '';
  if (_data.status == '待支付') {
    status_class = 'pending';
  } else if (_data.status == '支付成功') {
    status_class = 'success';
  } else {
    status_class = 'fail';
  }
  var _ui = '<li class="record-list-item clearfix">' +
              '<div class="item detail">' +
                '<p>'+ _data.title +'</p>' +
                '<p class="time">'+ _data.time +'</p>' +
              '</div>' +
              '<div class="item amout">'+ _data.amount +'</div>' +
              // '<div class="item status '+ status_class +'">'+ _data.status +'</div>' +
            '</li>';

  return _ui;
}
