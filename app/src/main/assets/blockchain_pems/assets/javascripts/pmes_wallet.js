$(document).ready(function() {
  var loading = new CreateLoader();
  loading.show();

  var get_native_data = {
    amount_active: '',
    amount_frozen: ''
  };

  if (typeof jwPut != 'undefined') {
    var native_data = jwPut.put_wallet_data();
    if (native_data != '') {
      get_native_data = JSON.parse(native_data);
    }
  }

  var amount_active = (get_native_data.amount_active * 1) / 10 ** 8;
  var amount_frozen = (get_native_data.amount_frozen * 1) / 10 ** 8;

  $('#amount_active').html(amount_active);
  $('#amount_frozen').html(amount_frozen);
  loading.destroy();
});

function dataFromNative(demo_data) {
  createAlert(demo_data);
}
