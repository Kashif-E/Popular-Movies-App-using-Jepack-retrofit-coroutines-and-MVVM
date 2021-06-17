package com.infinity.movieapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.accompanist.glide.rememberGlidePainter
import com.google.android.material.snackbar.Snackbar
import com.infinity.movieapp.models.domainmodel.asDataBaseModel
import com.infinity.movieapp.ui.MovieViewModel

class MovieDetailFragment : Fragment() {

    private val args: MovieDetailFragmentArgs by navArgs()
    private val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Image(
                        painter = rememberGlidePainter("https://image.tmdb.org/t/p/original/${args.movie.backdrop_path}"),
                        contentDescription = "BackDrop",
                        modifier = Modifier.height(300.dp),
                        contentScale = ContentScale.Crop,

                        )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = args.movie.title,
                                style = TextStyle(
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = "Rating ${args.movie.vote_average}",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color(0xFFDA291C),
                                    fontWeight = FontWeight.SemiBold

                                    ),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )

                        }


                        Spacer(modifier = Modifier.padding(top = 20.dp))
                        Image(
                            painter = rememberGlidePainter("https://image.tmdb.org/t/p/original/${args.movie.poster_path}"),
                            contentDescription = "Poster",
                            modifier = Modifier.height(400.dp),
                            contentScale = ContentScale.FillBounds

                        )

                        Spacer(modifier = Modifier.padding(top = 20.dp))
                        Text(
                            text = args.movie.overview,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            ),

                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.padding(top = 20.dp))
                        Text(
                            text = "Releasing On ${args.movie.release_date}",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            ),

                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.padding(top = 20.dp))

                        ExtendedFloatingActionButton(
                            text = {Text(text = "Add to saved")},
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = {
                                viewModel.saveMovie(args.movie.asDataBaseModel())

                                Snackbar.make(requireView(),
                                    "Movie Saved Successfully",
                                    Snackbar.LENGTH_SHORT).show()
                            },
                            icon = { Icon(Icons.Filled.Add, "Add Movie to Saved") }
                        )

                    }

                }
            }
        }
    }
}
/* lateinit var binding: FragmentMovieDetailBinding
 override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
     super.onViewCreated(view, savedInstanceState)

     binding = FragmentMovieDetailBinding.bind(view)

     binding.lifecycleOwner = viewLifecycleOwner
     binding.movie = args.movie

     binding.fab.setOnClickListener {
      viewModel.saveMovie(args.movie.asDataBaseModel())
         Snackbar.make(requireView(), "Movie Saved Successfully", Snackbar.LENGTH_SHORT).show()
     }
 }*/




