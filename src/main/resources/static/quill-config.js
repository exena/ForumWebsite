//**********************************************************************************************
//custom type 'imageBlot'
//var InlineBlot = Quill.import('blots/block');
//class ImageBlot extends InlineBlot {
//  static create(data) {
//    const node = super.create(data);
//    node.setAttribute('src', data.src);
//    node.setAttribute('data-filename', data.customfilename);
//    return node;
//  }
//  static value(domNode) {
//		const { src, customfilename } = domNode.dataset;
//		return { src, customfilename };
//	}
//}
//ImageBlot.blotName = 'imageBlot';
//ImageBlot.className = 'image-blot';
//ImageBlot.tagName = 'img';
//Quill.register({ 'formats/imageBlot': ImageBlot });
//**********************************************************************************************
//add attribute to type 'image'
//var Image = Quill.import('formats/image');
//Image.className = 'img-fluid';
//Quill.register(Image, true);
//**********************************************************************************************
//image resizer module
//Quill.register('modules/blotFormatter', QuillBlotFormatter.default);
//class CustomImageSpec extends QuillBlotFormatter.ImageSpec {
//    getActions() {
//        return [QuillBlotFormatter.AlignAction];//, QuillBlotFormatter.ResizeAction
//    }
//}
//**********************************************************************************************
//var quill = new Quill('#editor', {
//    //modules:{ toolbar: [ ['image','code-block'] ] },
//    modules:{ toolbar: [ ['image','code-block'] ], imageResize: {}, myModule:true },
//    theme: 'snow'
//});
//textarea quill editor
$('.quill-editor').each(function(i, el) {//index, element
    var el = $(this), id = 'quilleditor-' + i, val = el.val(), editor_height = '100%';
    var div = $('<div/>').attr('id', id).css('height', editor_height).css('font-size', '16px').html(val);
    el.addClass('d-none');
    el.parent().append(div);

    var quill = new Quill('#' + id, {
        modules:{ toolbar: '#toolbar'},
        theme: 'snow'
    });
//    quill.on('text-change', function() {
//        el.html(quill.root.innerHTML);
//    });
//**********************************************************************************************
// edit board with textarea quill
//    console.log(el.text());
//    const value = `<h1>New content here</h1>`;
    const delta = quill.clipboard.convert(el.text());
    quill.setContents(delta, 'silent');
//    quill.clipboard.dangerouslyPasteHTML(el.html());
//**********************************************************************************************
    quill.on('text-change', function() {
        el.html(quill.root.innerHTML);
    });
//**********************************************************************************************
// image input handler
    quill.getModule('toolbar').addHandler('image', function() {
        insertImageBlot();
    });

    var insertImageBlot = function () {
        const input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute("multiple","");
        input.click();

        input.onchange = function() {
            var fileArr = [];
            var readerArr = [];
            const range = quill.getSelection();
            for(var i=0; i < $(this)[0].files.length; i++){
                fileArr[i] = $(this)[0].files[i];
                readerArr[i] = new FileReader();
                readerArr[i].onload = function(){
                    var index = readerArr.indexOf(this);
                    quill.pasteHTML(range.index, `<img src="${readerArr[index].result}" class="img-fluid" filename="${fileArr[index].name}" alt="${fileArr[index].name}">`);
                    //quill.insertEmbed(range.index, 'imageBlot', {src: readerArr[index].result, customfilename: fileArr[index].name}, 'user');
                    //quill.setSelection(++range.index);
                };
                readerArr[i].readAsDataURL(fileArr[i]);
            }
        };

    };
//**********************************************************************************************
//onsubmit applying change
    document.getElementById('EntireForm').addEventListener('submit', function(){
        el.html(quill.root.innerHTML);
        alert("pause");
    });

});
//**********************************************************************************************