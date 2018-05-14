/**
 * @author zhixin wen <wenzhixin2010@gmail.com>
 * extensions: https://github.com/kayalshri/tableExport.jquery.plugin
 */

(function ($) {
    'use strict';
    var sprintf = $.fn.bootstrapTable.utils.sprintf;

   /* var TYPE_NAME = {
        json: 'JSON',
        xml: 'XML',
        png: 'PNG',
        csv: 'CSV',
        txt: 'TXT',
        sql: 'SQL',
        doc: 'MS-Word',
        excel: 'Excel',
        xlsx: 'MS-Excel (OpenXML)',
        powerpoint: 'MS-Powerpoint',
        pdf: 'PDF'
    };*/

    var TYPE_NAME = {
            excel: 'Excel'
        };
    $.extend($.fn.bootstrapTable.defaults, {
        showExport: false,
        exportDataType: 'basic', // basic, all, selected
        // 'json', 'xml', 'png', 'csv', 'txt', 'sql', 'doc', 'excel', 'powerpoint', 'pdf'
        exportTypes: ['json', 'xml', 'csv', 'txt', 'sql', 'excel'],
        exportOptions: {}
    });

    $.extend($.fn.bootstrapTable.defaults.icons, {
        export: 'glyphicon-export icon-share'
    });

    $.extend($.fn.bootstrapTable.locales, {
        formatExport: function () {
            return '导出';
        }
    });
    $.extend($.fn.bootstrapTable.defaults, $.fn.bootstrapTable.locales);

    var BootstrapTable = $.fn.bootstrapTable.Constructor,
        _initToolbar = BootstrapTable.prototype.initToolbar;

    BootstrapTable.prototype.initToolbar = function () {
        this.showToolbar = this.options.showExport;

        _initToolbar.apply(this, Array.prototype.slice.apply(arguments));

        if (this.options.showExport) {
            var that = this,
                $btnGroup = this.$toolbar.find('>.btn-group'),
                $export = $btnGroup.find('div.export');

            if (!$export.length) {
                $export = $([
                    '<div class="export btn-group">',
                        '<button class="btn' +
                            sprintf(' btn-%s', this.options.buttonsClass) +
                            sprintf(' btn-%s', this.options.iconSize) +
                            ' dropdown-toggle" aria-label="export type" ' +
                            'title="' + this.options.formatExport() + '" ' +
                            'data-toggle="dropdown" type="button" style="width:52px;height:34px;">',
                            sprintf('<i class="%s %s"></i> ', this.options.iconsPrefix, this.options.icons.export),
                            '<span class="caret"></span>',
                        '</button>',
                        '<ul class="dropdown-menu" role="menu">',
                        '</ul>',
                    '</div>'].join('')).appendTo($btnGroup);

                var $menu = $export.find('.dropdown-menu'),
                    exportTypes = this.options.exportTypes;

                if (typeof this.options.exportTypes === 'string') {
                    var types = this.options.exportTypes.slice(1, -1).replace(/ /g, '').split(',');

                    exportTypes = [];
                    $.each(types, function (i, value) {
                        exportTypes.push(value.slice(1, -1));
                    });
                }
                $.each(exportTypes, function (i, type) {
					   if (TYPE_NAME.hasOwnProperty(type)) {
						/* $menu.append([
										'<li role="menuitem" data-type="'+ type + '">',
										'<a href="javascript:void(0)">',TYPE_NAME[type] + " 1000条",'</a>',
										'</li>' ].join(''));*/
						 $menu.append([
										'<li role="menuitem" data-type="'+ type + '">',
										'<a href="javascript:void(0)">',TYPE_NAME[type] + " &nbsp;&nbsp;当前页",'</a>',
										'</li>',
										'<li role="menuitem" data-type="'+ type + '">',
										'<a href="javascript:void(0)">',TYPE_NAME[type] + " &nbsp;&nbsp;选中项",'</a>',
										'</li>',
										'<li role="menuitem" data-type="'+ type + '">',
										'<a href="javascript:void(0)">',TYPE_NAME[type] + " &nbsp;1000条",'</a>',
										'</li>' ].join(''));
						 
						
                    }
                });
                
                $menu.find('li').click(function () {
                    var type = $(this).data('type'),
                        doExport = function () {
                            
                            if (!!that.options.exportFooter) {
                                var data = that.getData();
                                var $footerRow = that.$tableFooter.find("tr").first();

                                var footerData = { };
                                var footerHtml = [];

                                $.each($footerRow.children(), function (index, footerCell) {
                                    
                                    var footerCellHtml = $(footerCell).children(".th-inner").first().html();
                                    footerData[that.columns[index].field] = footerCellHtml == '&nbsp;' ? null : footerCellHtml;

                                    // grab footer cell text into cell index-based array
                                    footerHtml.push(footerCellHtml);
                                });

                                that.append(footerData);

                                var $lastTableRow = that.$body.children().last();

                                $.each($lastTableRow.children(), function (index, lastTableRowCell) {

                                    $(lastTableRowCell).html(footerHtml[index]);
                                });
                            }
                            
                           /* that.$el.tableExport($.extend({}, that.options.exportOptions, {
                                type: type,
                                escape: false
                            }));*/
                            /* 忽略导出时，data-field="state":checkBox或者radio，行数据操作列：data-field="action"*/
                            var myOwnSetting={ignoreColumn: ['state','action']};
                            that.$el.tableExport($.extend(myOwnSetting, that.options.exportOptions, {
                                type: type,
                                escape: false
                            }));
                            
                            if (!!that.options.exportFooter) {
                                that.load(data);
                            }
                        };
                    /*修复导出数据时,选择所有导出BUG*/    
                    /*if (that.options.exportDataType === 'all' && that.options.pagination) {
                            that.$el.one(that.options.sidePagination === 'server' ? 'post-body.bs.table' : 'page-change.bs.table', function () {
                                doExport();
                                that.togglePagination();
                            });
                            that.togglePagination();
                    }*/ 
                     if ($(this).text().indexOf("1000条")!=-1 && that.options.pagination) {
                        	var myFixed=that.getOptions();
                        	if($.inArray(1000, myFixed.pageList)==-1){
                        		that.refreshOptions({pageSize:1000,pageNumber:myFixed.pageNumber,pageList:[10, 25, 50, 100,500,1000]});
                        	}else{
                        		that.refreshOptions({pageSize:1000,pageNumber:myFixed.pageNumber});
                        	}
                            that.$el.one(that.options.sidePagination === 'server' ? 'post-body.bs.table' : 'page-change.bs.table', function () {
                            	doExport();
                            	//that.togglePagination();
                            });
                            //that.togglePagination();
                      } else if ($(this).text().indexOf("选中项")!=-1) {
                            var data = that.getData(),
                                selectedData = that.getAllSelections();
                            // Quick fix #2220
                            if (that.options.sidePagination === 'server') {
                                data = {total: that.options.totalRows};
                                data[that.options.dataField] = that.getData();

                                selectedData = {total: that.options.totalRows};
                                selectedData[that.options.dataField] = that.getAllSelections();
                            }

                            that.load(selectedData);
                            doExport();
                            that.load(data);
                      } else {
                            doExport();
                     }   
                     /*修复导出数据时,选择所有(最多1000条)导出BUG*/       
                    /*if (that.options.exportDataType === 'all' && that.options.pagination) {
                    	var myFixed=that.getOptions();
                    	if($.inArray(1000, myFixed.pageList)==-1){
                    		that.refreshOptions({pageSize:1000,pageNumber:myFixed.pageNumber,pageList:[10, 25, 50, 100,500,1000]});
                    	}else{
                    		that.refreshOptions({pageSize:1000,pageNumber:myFixed.pageNumber});
                    	}
                        that.$el.one(that.options.sidePagination === 'server' ? 'post-body.bs.table' : 'page-change.bs.table', function () {
                        	doExport();
                        	//that.togglePagination();
                        });
                        //that.togglePagination();
                    } else if (that.options.exportDataType === 'selected') {
                        var data = that.getData(),
                            selectedData = that.getAllSelections();

                        // Quick fix #2220
                        if (that.options.sidePagination === 'server') {
                            data = {total: that.options.totalRows};
                            data[that.options.dataField] = that.getData();

                            selectedData = {total: that.options.totalRows};
                            selectedData[that.options.dataField] = that.getAllSelections();
                        }

                        that.load(selectedData);
                        doExport();
                        that.load(data);
                    } else {
                        doExport();
                    }*/
                });
            }
        }
    };
})(jQuery);
