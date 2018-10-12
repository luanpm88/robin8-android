document.addEventListener('touchstart', function() {}, true);

$(document).ready(function() {
  var native_put_address = '';
  var loading = new CreateLoader();
  // 重置密码
  $('#pmes_password_submit_btn').click(function(event) {
    var current_date = new Date();
    current_date = current_date.customFormat('#YYYY##MM##DD##hhh##mm#');
    var password = $('#password').val();
    var password_confirm = $('#password_confirm').val();
    var mnemonic = $('#mnemonic').val();

    if (password == '') {
      createAlert('请输入密码');
      return false;
    } else if (password != '' && !verify_pw.test(password)) {
      createAlert('请输入6位数字作为密码');
      return false;
    }
    if (password_confirm == '') {
      createAlert('请确认密码');
      return false;
    }
    if (password != password_confirm) {
      createAlert('两次密码输入不一致');
      return false;
    }
    if (mnemonic == '') {
      createAlert('请输入您的安全码');
      return false;
    }

    if (typeof jwPut != 'undefined') {
      native_put_address = jwPut.put_put_address();
    }
    loading.show();

    var pmes_recover = PMES.recover(mnemonic, password);
    var post_public_key = pmes_recover.public_key;
    // createAlert('public_key:'+pmes_recover.public_key);

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
          var put_address = put_put.address;

          if (put_address === native_put_address) {
            var post_native_data = {
              token: pmes_recover.token,
              public_key: post_public_key
            };
            post_native_data = JSON.stringify(post_native_data);
            if (typeof jwPut != 'undefined') {
              jwPut.put_recover(post_native_data);
            }
          } else {
            createAlert('mnemonic error');
          }
        } else {
          createAlert('没有钱包数据');
        }
      },
      error: function(xhr, type) {
        loading.destroy();
        createAlert('mnemonic error');
        console.log('mnemonic error');
      }
    });
  });
});

function dataFromNative(demo_data) {
  createAlert(demo_data);
}
