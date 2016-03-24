    var data = JSON.parse(window.java.getJson());

    var html = template.render('tpl_bbs', data);
    document.getElementById('div_bbs').innerHTML = html;
