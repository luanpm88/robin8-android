document.addEventListener('touchstart', function() {}, true);

$(document).ready(function() {
  var form_data = {};
  var phone_data = '';
  var device_id_data = '';
  var email_data = '';
  var current_token = '';

  if (typeof jwPut != 'undefined') {
    var native_data = jwPut.put_ToJs();
    if (native_data != '') {
      form_data = JSON.parse(native_data);
    }
    phone_data = form_data.phone_num;
    device_id_data = form_data.device_id;
    email_data = form_data.email;
    current_token = form_data.access_token;
  }

  $('#email').val(email_data);
  $('#phone').val(phone_data);
  $('#device_id').val(device_id_data);

  var email = $('#email').val();
  var phone = $('#phone').val();
  var device_id = $('#device_id').val();

  $('#protocol_checkbox').change(function() {
    var $that = $(this);
    judgeChecked($that, $('#pmes_open_btn'));
  });
  $('#pmes_open_btn').click(function(event) {
    $('#pmes_intro_page').fadeOut();
  });

  var loading = new CreateLoader();
  // 创建帐号
  $('#pmes_reg_submit_btn').click(function(event) {
    var current_date = new Date();
    current_date = current_date.customFormat('#YYYY##MM##DD##hhh##mm#');
    var password = $('#password').val();
    var password_confirm = $('#password_confirm').val();

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

    loading.show();

    var pmes_ctrl = new PMESCtrl(password, email, phone, device_id);
    console.log(pmes_ctrl);
    pmes_ctrl.sign();
    if (pmes_ctrl.error_tips === 'invaild token or password') {
      loading.destroy();
      createAlert('系统错误，请稍候');
      return false;
    }

    // createAlert('public_key:'+pmes_ctrl.public_key);

    var post_data = {
      'public_key': pmes_ctrl.public_key,
      'message': {
        'email': email, // Robin8用户电子邮件
        'phone': phone, // Robin8用户电话
        'device_id': device_id, // Robin8用户device_id
        'timestamp': current_date // 当前时间 201808091319, 格式 YYYYMMDDHHmm
      },
      'signature': pmes_ctrl.signature
    }
    post_data = JSON.stringify(post_data);
    console.log(post_data);

    // createAlert('post_data:'+post_data);
    // createAlert('domain:'+SERVERHOST);

    var pmes_mnemonic = pmes_ctrl.mnemonic;
    var mnemonic_tips = '<p>'+ pmes_mnemonic +'</p><br/><p class="font-xs">注意：若不保存，则有账号丢失风险</p>';
    var mnemonic_input = '<textarea class="" rows="4" id="mnemonic_input"></textarea>';

    $('#mnemonic_text').html(pmes_mnemonic);
    $('#pmes_mnemonic_page').fadeIn();

    loading.destroy();

    $('#pmes_mnemonic_get_btn').click(function(event) {
      createConfirm(
        mnemonic_input,
        {
          title: '请输入您的安全码',
          confirm: '确认',
          cancel: '忘记了'
        },
        function(type) {
          if (type == 'confirm') {
            loading.show();

            var mnemonic_input_val = $.trim($('#mnemonic_input').val());
            console.log(mnemonic_input_val);
            if (mnemonic_input_val === pmes_mnemonic) {
              $.ajax({
                url: URLHOST + '/api/accounts/',
                type: 'POST',
                data: post_data,
                success: function(data) {
                  console.log(data);
                  console.log(JSON.parse(data.wallets));

                  var wallets_data = JSON.parse(data.wallets);
                  var put_put = '';
                  $.each(wallets_data, function(index, el) {
                    if (el.coinid === 'PUT') {
                      put_put = el;
                    }
                  });

                  console.log(put_put.address);

                  $.ajax({
                    url: SERVERHOST + 'pages/bind_e_wallet',
                    type: 'POST',
                    beforeSend: function(xhr) {
                      xhr.setRequestHeader('Authorization', current_token);
                    },
                    data: {
                      put_address: put_put.address
                    },
                    success: function(data) {
                      loading.destroy();
                      // createAlert('ruby post success:' + data.put_address);
                      var post_native_data = {
                        token: pmes_ctrl.token,
                        public_key: pmes_ctrl.public_key,
                        password: password,
                        put_address: data.put_address,
                        amount_active: put_put.amount_active,
                        amount_frozen: put_put.amount_frozen
                      };
                      console.log(post_native_data);
                      post_native_data = JSON.stringify(post_native_data);
                      if (typeof jwPut != 'undefined') {
                        jwPut.put_reg(post_native_data);
                      }
                    },
                    error: function(xhr, type) {
                      loading.destroy();
                      createAlert('ruby post error');
                      console.log('error');
                    }
                  });
                },
                error: function(xhr, type) {
                  loading.destroy();
                  createAlert('pmes post error');
                  console.log('error');
                }
              });
            } else {
              loading.destroy();
              createAlert('Mnemonic不正确');
            }
          }
        }
      )
    });
  });
});

function dataFromNative(demo_data) {
  createAlert(demo_data);
}
