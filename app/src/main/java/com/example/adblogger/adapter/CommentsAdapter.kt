import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adblogger.R
import com.example.adblogger.model.Comment

class CommentsAdapter(private val commentsList: List<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentTextView: TextView = itemView.findViewById(R.id.commentText)
        val senderTextView: TextView = itemView.findViewById(R.id.senderText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false) // Replace with your comment item layout
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentData = commentsList[position]
        holder.commentTextView.text = commentData.comment
        holder.senderTextView.text = commentData.senderEmail
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }
}
