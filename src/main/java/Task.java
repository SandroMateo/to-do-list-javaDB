import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import org.sql2o.*;

public class Task {
  private String description;
  private boolean completed;
  private LocalDateTime createdAt;
  private int id;
  private int categoryId;

  public Task(String description, int categoryId) {
    this.description = description;
    completed = false;
    createdAt = LocalDateTime.now();
    this.categoryId = categoryId;
  }

  public String getDescription() {
    return description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed, int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE tasks SET completed = :completed WHERE id = :id";
      con.createQuery(sql)
        .addParameter("completed", completed)
        .addParameter("id", id)
        .executeUpdate();
    }
    this.completed = completed;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public static List<Task> all() {
    String sql = "SELECT id, description, categoryId FROM tasks";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Task.class);
    }
  }

  public int getId() {
    return id;
  }

  public static Task find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks where id=:id";
      Task task = con.createQuery(sql).addParameter("id", id).executeAndFetchFirst(Task.class);
      return task;
    }
  }

  public void save() {
   try(Connection con = DB.sql2o.open()) {
     String sql = "INSERT INTO tasks (description, categoryId, completed) VALUES (:description, :categoryId, :completed)";
     this.id = (int) con.createQuery(sql, true)
       .addParameter("description", this.description)
       .addParameter("categoryId", this.categoryId)
       .addParameter("completed", this.completed)
       .executeUpdate()
       .getKey();
   }
 }


  @Override
  public boolean equals(Object otherTask) {
    if (!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask = (Task) otherTask;
      return this.getDescription().equals(newTask.getDescription()) && this.getId() == newTask.getId() && this.getCategoryId() == newTask.getCategoryId();
    }
  }




}
