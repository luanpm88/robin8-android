var verify_phone = /^(0|86|17951)?(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/;  // 手机号码验证
var verify_email = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/; // email校验
var verify_pw = /^\d{6}$/; //密码校验，6位数字

var SERVERHOST = 'https://robin8.net/';
// var SERVERHOST = 'https://qa.robin8.net/';
// var SERVERHOST = 'http://192.168.50.176:3000/';
// var URLHOST = 'http://pdms2.robin8.io';
var URLHOST = 'https://pmes.robin8.io';

$(document).ready(function() {
  // 标签切换
  $('.tab-control').each(function() {
    var $tab_control = $(this);
    $tab_control.find('.tab-ctrl-tag').on('click', '.item', function() {
      var $that = $(this),
          index = $that.index(),
          $tags = $tab_control.find('.tab-ctrl-tag'),
          $content = $tab_control.find('.tab-content');
      $that.siblings('.item').removeClass('active');
      $that.addClass('active');
      $content.eq(index).siblings('.tab-content').removeClass('active');
      $content.eq(index).addClass('active');
    });
  });

  // 折叠标签
  $('.collapse-panel').each(function() {
    var $panel = $(this),
        $siblings_panel = $panel.siblings('.collapse-panel'),
        $collapse_tab = $panel.find('.collapse-panel-tab'),
        $collapse_list = $panel.find('.collapse-panel-content');
    $panel.on('click', '.collapse-panel-tab', function() {
      if ($collapse_tab.hasClass('open')) {
        $collapse_list.removeClass('open');
        $collapse_tab.removeClass('open');
      } else {
        $collapse_tab.addClass('open');
        $collapse_list.addClass('open');
        $siblings_panel.find('.collapse-panel-tab').removeClass('open');
        $siblings_panel.find('.collapse-panel-content').removeClass('open');
      }
    });
  });

  // 返回顶部
  $('#back_top_btn').hide();
  $(window).scroll(function () {
    if ($(window).scrollTop() >= 100) {
      $('#back_top_btn').show();
    } else {
      $('#back_top_btn').hide();
    }
  });
  $('#back_top_btn').click(function () {
    $('html, body').animate({ scrollTop: 0 }, 500);
  });
});

// 判断复选框是否被选中，改变button状态
function judgeChecked(checkbox, button) {
  if (!checkbox.is(':checked')) {
    button.attr('disabled', true);
  } else {
    button.attr('disabled', false);
  }
}

// 时间格式化
Date.prototype.customFormat = function(formatString) {
  var YYYY, YY, MMMM, MMM, MM, M, DDDD, DDD, DD, D, hhhh, hhh, hh, h, mm, m, ss, s, ampm, AMPM, dMod, th;
  var dateObject = this;
  YY = ((YYYY = dateObject.getFullYear()) + '').slice(-2);
  MM = (M = dateObject.getMonth() + 1) < 10 ? ('0' + M) : M;
  MMM = (MMMM = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][M - 1]).substring(0, 3);
  DD = (D = dateObject.getDate()) < 10 ? ('0' + D) : D;
  DDD = (DDDD = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][dateObject.getDay()]).substring(0, 3);
  th = (D >= 10 && D <= 20) ? 'th' : ((dMod = D % 10) == 1) ? 'st' : (dMod == 2) ? 'nd' : (dMod == 3) ? 'rd' : 'th';
  formatString = formatString
                .replace('#YYYY#', YYYY)
                .replace('#YY#', YY)
                .replace('#MMMM#', MMMM)
                .replace('#MMM#', MMM)
                .replace('#MM#', MM)
                .replace('#M#', M)
                .replace('#DDDD#', DDDD)
                .replace('#DDD#', DDD)
                .replace('#DD#', DD)
                .replace('#D#', D)
                .replace('#th#', th);

  h = (hhh = dateObject.getHours());
  if (h == 0) {
    h = 24;
  }
  if (h > 12) {
    h -= 12;
  }
  hh = h < 10 ? ('0' + h) : h;
  hhhh = hhh < 10 ? ('0' + hhh) : hhh;
  AMPM = (ampm = hhh < 12 ? 'am' : 'pm').toUpperCase();
  mm = (m = dateObject.getMinutes()) < 10 ? ('0' + m) : m;
  ss = (s = dateObject.getSeconds()) < 10 ? ('0' + s) : s;
  return formatString
        .replace('#hhhh#', hhhh)
        .replace('#hhh#', hhh)
        .replace('#hh#', hh)
        .replace('#h#', h)
        .replace('#mm#', mm)
        .replace('#m#', m)
        .replace('#ss#', ss)
        .replace('#s#', s)
        .replace('#ampm#', ampm)
        .replace('#AMPM#', AMPM);
}

// 获取URL参数
function GetRequest() {
  var url = window.location.search; //获取url中"?"符后的字串
  var theRequest = new Object();
  if (url.indexOf('?') != -1) {
    var str = url.substr(1);
    strs = str.split('&');
    for (var i = 0; i < strs.length; i ++) {
      theRequest[strs[i].split('=')[0]] = decodeURI(strs[i].split('=')[1]);
    }
  }
  return theRequest;
}

/**
 * 创建公用全屏loading样式
 * 说明：可直接用createLoader()方法调用；
 * 其中参数tips用来显示提示信息，非必填，默认显示‘加载中’
 * 其中参数status用来设置停留时间，非必填，默认不填，默认非自动关闭，如果需要自动关闭，请设置为{auto_off: true, dely: 2000}
 * 示例：createLoader('请等待')；
 * .destroy()方法可用来隐藏提示框。
 */
function CreateLoader(tips, status) {
  this.dom = '';
  this.tips = tips;

  if (!!status) {
    this.status = {
      auto_off: status.auto_off,
      dely: status.dely
    };
  }
}

CreateLoader.prototype.createDom = function() {
  var tips = this.tips;
  if (!tips || typeof(tips) === 'undefined') {
    tips = '';
  }
  var dom = '<div class="loading-container">' +
              '<div class="mask"></div>' +
              '<div class="loader">' +
                '<div class="lds-css ng-scope">' +
                  '<div class="lds-rolling">' +
                    '<div></div>' +
                  '</div>' +
                '</div>' +
                '<div class="tips">'+ tips +'</div>' +
              '</div>' +
            '</div>';
  return dom;
}

CreateLoader.prototype.show = function() {
  var that = this,
      $dom = $(that.createDom());
  $('body').append($dom);
  $dom.fadeIn();
  that.dom = $dom;

  if (!!that.status) {
    if (!that.status.dely || that.status.dely === '' || typeof(that.status.dely) === 'undefined') {
      that.status.dely = 1000;
    }

    if (!!that.status.auto_off) {
      setTimeout(function() {
        that.destroy();
      }, that.status.dely);
    }
  }
}

CreateLoader.prototype.destroy = function() {
  var $dom = $(this.dom);
  $dom.fadeOut(function () {
    $dom.remove();
  });
}

/**
 * 创建公用单独按钮alert提示框
 * 说明：可直接用createAlert()方法调用；
 * 其中message参数为必填参数，用来显示提示信息，
 * 示例：createAlert('数据错误！')；
 * opt和callback为选填参数：
 * opt默认为一个对象，也可为一个返回函数，当作为一个对象时，可设置提示框标题及按钮文字，
 * 示例：createAlert('数据错误！', {title: '请注意！', btn: '我知道了'})；
 * 当opt作为一个返回函数时，title和btn会有一个默认的显示，而后一个callback参数就没有必要再做设置。
 * 示例：createAlert('数据错误！', function(){……})；
 * callback默认为一个返回函数，作用于点击alert按钮后的操作。
 * 示例：createAlert('数据错误！', {title: '请注意！', btn: '我知道了'}, function(){……})；
 * .destroy()方法可用来隐藏提示框。
 */
var createAlert = function (message, opt, callback){
  if(window.BKBridge){
    var nopt={"msg":message};
    if (typeof opt === 'object'){
      nopt.data=opt;
    }
    show_alert(nopt, callback);
    return {};
  }
  var alertModal = new Object();
  alertModal.message = message;
  alertModal.opt = opt;
  alertModal.createHtml = function (){
    var html = '<div class="alert-modal">'
             + '<div class="modal-cover"></div>'
             + '<div class="modal-content">';
    if (typeof this.title !== 'undefined'){
      html += '<h5 class="modal-header">'+ this.title +'</h5>';
    } else {
      html += '<h5 class="modal-header">请注意</h5>';
    }
    if (typeof this.message !== 'undefined'){
      html += '<div class="modal-body">'+ this.message +'</div>';
    }
    if (typeof this.btn !== 'undefined'){
      html += '<div class="modal-footer"><div class="btn btn-confirm">'+ this.btn +'</div></div>';
    } else {
      html += '<div class="modal-footer"><div class="btn btn-confirm">确定</div></div>';
    }
    html += '</div></div>';
    return html;
  };
  alertModal.applyCallback = function (){
    if (typeof this.callback === 'function'){
      var fn = this.callback;
      fn();
    };
  };
  alertModal.createDom = function (){
    var dom = this.createHtml();
    this.modalDom = $(dom);
  };
  alertModal.setTitle = function (){
    var title = this.opt.title;
    if (typeof title === 'string') {
      // 定义title
      this.title = title;
    }
  };
  alertModal.setButton = function (){
    var btn = this.opt.btn;
    if (typeof btn === 'string') {
      // 定义btn
      this.btn = btn;
    }
  };
  alertModal.setCallback = function (fn){
    if (typeof fn === 'function') {
      // 定义callback
      this.callback = fn;
    }
  };
  alertModal.buildOpt = function (){
    if (typeof this.opt === 'object'){
      // 代表是title和btn对象
      this.setTitle();
      this.setButton();
    } else if (typeof this.opt === 'function'){
      // 代表opt是个callback
      this.setCallback(this.opt);
    }
  };
  alertModal.show = function (){
    // 编辑title配置
    this.buildOpt();
    // 设置callback参数
    this.setCallback(callback);
    this.createDom();
    this.bindClick();
    $('body').append(this.modalDom);
    this.modalDom.fadeIn().addClass('active');
  };
  alertModal.destroy = function (){
    this.modalDom.removeClass('active').fadeOut(function(){
      this.modalDom.remove();
    }.bind(this));
  };
  alertModal.bindClick = function (){
    this.modalDom.find('.modal-cover').click(function(){
      this.destroy();
    }.bind(this));
    this.modalDom.find('.btn-confirm').click(function(){
      this.destroy();
      this.applyCallback();
    }.bind(this));
  };
  alertModal.show();
};

/**
 * 创建公用确定、取消双按钮并有返回函数confirm提示框
 * 说明：可直接用createConfirm()方法调用；
 * 其中message参数为必填参数，用来显示提示信息，
 * 示例：createConfirm('您是否要继续？')；
 * opt和callback为选填参数：
 * opt默认为一个对象，也可为一个返回函数，当作为一个对象时，可设置提示框标题及两个按钮文字，
 * confirm来设置确定按钮，cancel来设置取消按钮：
 * 示例：createConfirm('您是否要继续？', {title: '请注意！', confirm: '好的', cancel: '否'})；这三个参数均为选填，当不设置时，均有默认文字显示。
 * 当opt作为一个返回函数时，title和btn都会有一个默认的显示，而后一个callback参数就没有必要再做设置。
 * 返回函数需要一个返回值来判定用户点击的是confirm还是cancel；
 * 示例：createConfirm('您是否要继续？', function(type){if(type == 'confirm'){'点击了是'};if(type == 'cancel'){'点击了否'}})；
 * callback默认为一个返回函数，作用于点击按钮后的操作，具体用法同上；
 * 示例：createConfirm('您是否要继续？', function(type){if(type == 'confirm'){'点击了是'};if(type == 'cancel'){'点击了否'}})；
 * .destroy()方法可用来隐藏提示框。
 */
var createConfirm = function (message, opt, callback){
  var confirmModal = new Object();
  confirmModal.message = message;
  confirmModal.opt = opt;
  confirmModal.createHtml = function (){
    var html = '<div class="alert-modal">'
             + '<div class="modal-cover"></div>'
             + '<div class="modal-content">';
    if (typeof this.title !== 'undefined'){
      html += '<h5 class="modal-header">'+ this.title +'</h5>';
    } else {
      html += '';
    }
    if (typeof this.message !== 'undefined'){
      html += '<div class="modal-body">'+ this.message +'</div>';
    } else {
      html += '<div class="modal-body">确认是否继续？</div>';
    }
    if (typeof this.cancel_btn !== 'undefined'){
      html += '<div class="modal-footer"><div class="btn btn-cancel">'+ this.cancel_btn +'</div>';
    } else {
      html += '<div class="modal-footer"><div class="btn btn-cancel">取消</div>';
    }
    if (typeof this.confirm_btn !== 'undefined'){
      html += '<div class="btn btn-confirm">'+ this.confirm_btn +'</div></div>';
    } else {
      html += '<div class="btn btn-confirm">确定</div></div>';
    }
    html += '</div></div>';
    return html;
  };
  confirmModal.createDom = function (){
    var dom = this.createHtml();
    this.modalDom = $(dom);
  };
  confirmModal.setTitle = function (){
    var title = this.opt.title;
    if (typeof title === 'string') {
      // 定义title
      this.title = title;
    }
  };
  confirmModal.setButton = function (){
    var confirm = opt.confirm;
    var cancel = opt.cancel;
    if (typeof confirm === 'string'){
      this.confirm_btn = confirm;
    }
    if (typeof cancel === 'string'){
      this.cancel_btn = cancel;
    }
  };
  confirmModal.setCallback = function (fn){
    if (typeof fn === 'function') {
      // 定义callback
      this.callback = fn;
    }
  };
  confirmModal.applyCallback = function (type){
    if (typeof this.callback === 'function'){
      var fn = this.callback;
      fn(type);
    };
  };
  confirmModal.buildOpt = function (){
    if (typeof this.opt === 'object'){
      // 代表是title和btn对象
      this.setTitle();
      this.setButton();
    } else if (typeof this.opt === 'function'){
      // 代表opt是个callback
      this.setCallback(this.opt);
    }
  };
  confirmModal.show = function (){
    // 编辑title配置
    this.buildOpt();
    // 设置callback参数
    this.setCallback(callback);
    this.createDom();
    this.bindClick();
    $('body').append(this.modalDom);
    this.modalDom.fadeIn().addClass('active');
  };
  confirmModal.destroy = function (){
    this.modalDom.removeClass('active').fadeOut(function(){
      this.modalDom.remove();
    }.bind(this));
  };
  confirmModal.bindClick = function (){
    // this.modalDom.find('.modal-cover').click(function(){
    //   this.destroy();
    // }.bind(this));
    this.modalDom.find('.btn-cancel').click(function(){
      this.destroy();
      this.applyCallback('cancel');
    }.bind(this));
    this.modalDom.find('.btn-confirm').click(function(){
      this.destroy();
      this.applyCallback('confirm');
    }.bind(this));
  };
  confirmModal.show();
};

// 拖动刷新加载方法
function DropLoadCtrl(container, list, url, params, token, create_item, empty_icon) {
  var that = this;
  that.dropload = $(container).dropload({
    scrollArea: window,
    refreshFn: function(me) {
      params.page = 1;
      $.ajax({
        url: url,
        type: 'GET',
        data: params,
        beforeSend: function(xhr) {
          xhr.setRequestHeader('Authorization', token);
        },
        success: function(data) {
          var _data = data.list;
          $(container).find(list).empty();
          if (!!_data && _data.length > 0) {
            $.each(_data, function(index, el) {
              $(container).find(list).append(create_item(el));
            });
          } else {
            me.lock();
            if (params.page == 1) {
              me.emptyData();
              $(container).find(list).append(
                createEmptyContent(empty_icon)
              );
            } else {
              me.noData();
            }
            console.log('no data');
          }
          setTimeout(function () {
            me.resetload();
            console.log('reset load');
          }, 1000);
        },
        error: function(xhr, type) {
          console.log('post data error');
          me.resetload();
        }
      });
    },
    loadUpFn: function(me) {
      params.page = 1;
      $.ajax({
        url: url,
        type: 'GET',
        data: params,
        beforeSend: function(xhr) {
          xhr.setRequestHeader('Authorization', token);
        },
        success: function(data) {
          var _data = data.list;
          if (!!_data && _data.length > 1) {
            $(container).find(list).empty();
            $.each(_data, function(index, el) {
              $(container).find(list).append(create_item(el));
            });
          } else {
            console.log('no data');
          }
          setTimeout(function () {
            me.resetload();
            params.page = 1;
            me.unlock();
            me.noData(false);
            console.log('reset load');
          }, 1000);
        },
        error: function(xhr, type) {
          console.log('post data error');
          me.resetload();
        }
      });
    },
    loadDownFn: function (me) {
      params.page = params.page += 1;

      $.ajax({
        url: url,
        type: 'GET',
        data: params,
        beforeSend: function(xhr) {
          xhr.setRequestHeader('Authorization', token);
        },
        success: function(data) {
          var _data = data.list;
          console.log(_data);
          if (!!_data && _data.length > 0) {
            $.each(_data, function(index, el) {
              $(container).find(list).append(create_item(el));
            });
          } else {
            me.lock();
            if (params.page == 1) {
              me.emptyData();
              $(container).find(list).append(
                createEmptyContent(empty_icon)
              );
            } else {
              me.noData();
            }
            console.log('no data');
          }
          setTimeout(function () {
            me.resetload();
            console.log('reset load');
          }, 1000);
        },
        error: function(xhr, type) {
          console.log('post data error');
          me.resetload();
        }
      });
    }
  });
}

// 构建对象
function buildObjData(key, value) {
  var obj = {};
  obj[key] = value;
  return obj;
}

// localstorage事件
function setLocalData(key, value) {
  var storage = window.localStorage;
  storage.setItem(key, JSON.stringify(value));
}

function getLocalData(key) {
  var storage = window.localStorage;
  return JSON.parse(storage.getItem(key));
}

function removeLocalData(key) {
  var storage = window.localStorage;
  storage.removeItem(key);
}

function clearLocalData() {
  var storage = window.localStorage;
  storage.clear();
}

// 只准输入数字
function keyboardOnlyNumber(){
  var keyCode = event.keyCode;
  if (keyCode >= 48 && keyCode <= 57) {
    event.returnValue = true;
  }else {
    event.returnValue = false;
  }
}

// 检查单数据是否为undefined
function checkData(data) {
  data = !!data ? data : '';
  return data;
}

// 用key获取数据
function keyGetData(data, key) {
  var result = {};
  if (data.key === key) {
    result.key = data.key;
    result.title = data.title;
    result.content = data.content;
  }
  return result;
}

function PMESCtrl(password, email, phone, device_id) {
  this.password = password;
  this.email = email;
  this.phone = phone;
  this.device_id = device_id;

  this.create = PMES.create(this.password);
  this.token = this.create.token;
  this.public_key = this.create.public_key;
  this.mnemonic = this.create.mnemonic;
  this.signature = '';
  this.error_tips = '';
}

PMESCtrl.prototype = {
  constructor: PMESCtrl,
  sign: function() {
    var current_date = new Date();
    current_date = current_date.customFormat('#YYYY##MM##DD##hhh##mm#');
    var that = this;
    var pmes_sign = PMES.sign(
      that.token,
      {
        'message': {
          'email': that.email, // Robin8用户电子邮件
          'phone': that.phone, // Robin8用户电话
          'device_id': that.device_id, // Robin8用户device_id
          'timestamp': current_date // 当前201808091319, 格式 YYYYMMDDHHmm
        }
      },
      that.password
    );

    if (Object.keys(pmes_sign).indexOf('error') > -1) {
      that.error_tips = 'invaild token or password';
      return false;
    }
    that.signature = pmes_sign.signature;
  }
}

// 复制粘贴优化
document.querySelectorAll('input, textarea').forEach(function(ele) {
  ele.addEventListener('paste',
  function(event) {
    // 输入框类型
    var type = this.getAttribute('type') || this.type;
    // 剪切板数据对象
    var clipboardData = event.clipboardData || window.clipboardData;
    // 粘贴内容
    var paste = '';
    // 剪切板对象可以获取
    if (!clipboardData) {
      return;
    }
    // 获取选中的文本内容
    var userSelection, textSelected = '';
    if (window.getSelection) {
      // 现代浏览器
      // 直接window.getSelection().toString()对于IE的输入框无效
      textSelected = this.value.slice(ele.selectionStart, ele.selectionEnd);
    } else if (document.selection) {
      // 旧IE浏览器
      textSelected = document.selection.createRange().text;
    }
    // 只有输入框没有数据，或全选状态才处理
    if (this.value.trim() == '' || textSelected === this.value) {
      // 阻止冒泡和默认粘贴行为
      event.preventDefault();
      event.stopPropagation();
      // 获取粘贴数据
      paste = clipboardData.getData('text') || '';
      // 进行如下处理
      // 除非是password，其他都过滤前后空格
      if (type != 'password') {
        paste = paste.trim();
      }
      // 邮箱处理，可能会使用#代替@避免被爬虫抓取
      if (type == 'email') {
        paste = paste.replace('#', '@');
      } else if (type == 'tel') {
        // 手机号处理
        paste = paste.replace(/^\+86/, '');
        // 如果此时剩余所有数字正好11位
        if (paste.match(/\d/) && paste.match(/\d/g).length == 11) {
          paste = paste.replace(/\D/g, '');
        }
      } // 其他类型处理大家自行补充...
      // 插入
      this.value = paste;
    }
  });
});
