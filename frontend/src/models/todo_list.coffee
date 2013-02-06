define ['bacon', 'backbone', 'underscore', 'models/todo', 'localstorage'], (Bacon, Backbone, _, Todo) ->

  class TodoList extends Backbone.Collection
    model: Todo
    url: 'http://localhost:8000/todos'
    #localStorage: new Backbone.LocalStorage('todos-bacon')

    toggleAll: (completed) -> @each (todo) -> todo.save completed: completed

    initialize: ->
      @changed       = @asEventStream("add remove reset change").map(this).toProperty()
      @open          = @changed.map => @reject (t) -> t.get 'completed'
      @completed     = @changed.map => @filter (t) -> t.get 'completed'
      @someCompleted = @changed.map => @some   (t) -> t.get 'completed'
      @allCompleted  = @changed.map => @every  (t) -> t.get 'completed'
      @notEmpty      = @changed.map => @length > 0
