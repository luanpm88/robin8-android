$(document).ready(function() {
  var loading = new CreateLoader();
  loading.show();

  var get_native_data = {
    amount_active: '',
    amount_frozen: '',
    current_token: ''
  };

  if (typeof jwPut != 'undefined') {
    var native_data = jwPut.put_wallet_data();
    if (native_data != '') {
      get_native_data = JSON.parse(native_data);
    }
  }

  var amount_active = (get_native_data.amount_active * 1) / Math.pow(10, 8);
  var amount_frozen = (get_native_data.amount_frozen * 1) / Math.pow(10, 8);

  $('#amount_active').html(amount_active);
  $('#amount_frozen').html(amount_frozen);

  $.ajax({
    url: SERVERHOST + 'api/v2_0/e_wallets/unpaid',
    type: 'GET',
    beforeSend: function(xhr) {
      xhr.setRequestHeader('Authorization', get_native_data.current_token);
    },
    success: function(data) {
      loading.destroy();
      console.log(data);
      if (!!data.amount && data.amount != '') {
        $('#wallet_amount').html(data.amount);
      } else {
        $('#wallet_amount').html('0');
      }
    },
    error: function(xhr, type) {
      loading.destroy();
      console.log('post data error');
    }
  });
});

function dataFromNative(demo_data) {
  createAlert(demo_data);
}
