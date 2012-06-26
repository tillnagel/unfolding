$(document).ready(function(){
	$('.carousel').carousel();
	$('pre code').each(function(){
    var replacecontent = $(this).html();
    $(this).replaceWith(replacecontent);
	})
	$('pre').each(function(){
	  $(this).addClass('brush: processing; toolbar: false;');
	})
	SyntaxHighlighter.all()
});