Ext.namespace('Ext.ux.Andrie');

/**
 * 
 * @class Ext.ux.Andrie.pPageSize
 * 
 * @extends Ext.PagingToolbar
 * 
 * A combobox control that glues itself to a PagingToolbar's pageSize
 * configuration property.
 * 
 * @constructor
 * 
 * Create a new PageSize plugin.
 * 
 * @param {Object}
 *          config Configuration options
 * 
 * @author Andrei Neculau - andrei.neculau@gmail.com /
 *         http://andreineculau.wordpress.com
 * 
 * @version 0.6
 * 
 */

Ext.ux.Andrie.pPageSize = Ext.extend(Object, {

  /**
   * @cfg {Ext.data.Store} options
   * The {@link Ext.data.Store} combobox should use as its data source (required).
   * You can also use an array of integers.
   * Defaults to [5, 10, 15, 20, 25, 30, 50, 75, 100, 200, 300, 500, 1000]
   */	
    options: [ 20,  30, 50, 100, 200, 500, 1000],
  
  /**
   * @cfg {String} mode Acceptable values are:
   * 
   * 
	'remote' : Default
   * 
	Automatically loads the {@link #store} the first time the trigger
   * is clicked. If you do not want the store to be automatically loaded the first time the trigger is
   * clicked, set to 'local' and manually load the store.  To force a requery of the store
   * every time the trigger is clicked see {@link #lastQuery}.
   * 
	'local' :
   * 
	ComboBox loads local data
   * 
   * 
   */
  mode: 'remote',
  
  /**
   * @cfg {String} displayText
   * The message to display before the combobox (defaults to 'Records per Page')
   */
  displayText: 'ÿҳ',
  
  /**
   * @cfg {Boolean} prependCombo
   * true to insert the combobox before the paging buttons.
   * Defaults to false.
   */
  prependCombo: false,

  constructor: function(config){
	
    Ext.apply(this, config);
    Ext.ux.Andrie.pPageSize.superclass.constructor.call(this, config);
  },

  init : function(pagingToolbar) {
	
	var comboStore = this.options;
	  
    var combo = new Ext.form.ComboBox({
      typeAhead: false,
      triggerAction: 'all',
      forceSelection: true,
      selectOnFocus:true,
      lazyRender:true,
      editable: false,
      mode: this.mode,
      value: pagingToolbar.pageSize,
      width:50,
      store: comboStore,
      listeners: {
        select: function(combo, value, i){
          pagingToolbar.pageSize = comboStore[i];
          pagingToolbar.doLoad(Math.floor(pagingToolbar.cursor/pagingToolbar.pageSize)*pagingToolbar.pageSize);
        }
      }
    });

    var index = 0;
    
    if (this.prependCombo){
    	index = pagingToolbar.items.indexOf(pagingToolbar.first);
    	index--;
    } else{
    	index = pagingToolbar.items.indexOf(pagingToolbar.refresh);
        pagingToolbar.insert(++index,'-');
    }
    
    pagingToolbar.insert(++index, this.displayText);
    pagingToolbar.insert(++index, combo);
    
    if (this.prependCombo){
    	 pagingToolbar.insert(++index,'-');
    }
    
    //destroy combobox before destroying the paging toolbar
    pagingToolbar.on({
      beforedestroy: function(){
    	combo.destroy();
      }
    });

  }
});