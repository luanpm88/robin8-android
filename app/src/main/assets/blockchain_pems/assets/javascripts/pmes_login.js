document.addEventListener('touchstart', function() {}, true);

$(document).ready(function() {
  // 已有帐号
  var loading = new CreateLoader();
  $('#login_password').pwd('init');
  $('#pmes_login_btn').click(function(event) {
    var current_date = new Date();
    current_date = current_date.customFormat('#YYYY##MM##DD##hhh##mm#');
    var login_password = $('#login_password').val();

    if (login_password == '') {
      createAlert('请输入密码');
      return false;
    }

    loading.show();

    var get_native_data = {
      token: '',
      public_key: ''
    };

    if (typeof jwPut != 'undefined') {
      var native_data = jwPut.put_pmes_data();
      if (native_data != '') {
        get_native_data = JSON.parse(native_data);
      }
    }

    var post_token = get_native_data.token.toString();
    var post_public_key = get_native_data.public_key.toString();

    var pmes_sign = PMES.sign(
      post_token,
      {
        'message': {
          'timestamp': current_date // 当前201808091319, 格式 YYYYMMDDHHmm
        }
      },
      login_password
    );

    console.log(pmes_sign);

    if (Object.keys(pmes_sign).indexOf('error') > -1) {
      $('#login_password').pwd('reset');
      loading.destroy();
      createAlert('密码错误，请重新输入');
      return false;
    }
    var post_data = {
      'public_key': post_public_key,
      'message': {
        'timestamp': current_date // 当前时间 201808091319, 格式 YYYYMMDDHHmm
      },
      'signature': pmes_sign.signature
    };

    $.ajax({
      url: URLHOST + '/api/accounts/'+ post_public_key +'/',
      type: 'GET',
      success: function(data) {
        loading.destroy();
        if (!!data.wallets) {
          var wallets_data = JSON.parse(data.wallets);
          var put_put = '';
          $.each(wallets_data, function(index, el) {
            if (el.coinid === 'PUT') {
              put_put = el;
            }
          });

          var post_native_data = {
            amount_active: put_put.amount_active,
            amount_frozen: put_put.amount_frozen
          };
          console.log(post_native_data);
          post_native_data = JSON.stringify(post_native_data);
          if (typeof jwPut != 'undefined') {
            jwPut.put_login(post_native_data);
          }
        } else {
          createAlert('没有钱包数据');
        }
      },
      error: function(xhr, type) {
        loading.destroy();
        console.log('post data error');
      }
    });
  });
});

function dataFromNative(demo_data) {
  createAlert(demo_data);
}
