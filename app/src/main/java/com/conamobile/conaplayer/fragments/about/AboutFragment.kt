package com.conamobile.conaplayer.fragments.about

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.conamobile.conaplayer.Constants
import com.conamobile.conaplayer.R
import com.conamobile.conaplayer.adapter.ContributorAdapter
import com.conamobile.conaplayer.databinding.FragmentAboutBinding
import com.conamobile.conaplayer.extensions.openUrl
import com.conamobile.conaplayer.fragments.LibraryViewModel
import dev.chrisbanes.insetter.applyInsetter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AboutFragment : Fragment(R.layout.fragment_about), View.OnClickListener {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAboutBinding.bind(view)
        binding.aboutContent.cardOther.version.setSummary(getAppVersion())
        setUpView()
        loadContributors()

        binding.aboutContent.root.applyInsetter {
            type(navigationBars = true) {
                padding(vertical = true)
            }
        }
    }

    private fun setUpView() {
        binding.aboutContent.cardRetroInfo.faqLink.setOnClickListener(this)
        binding.aboutContent.cardRetroInfo.appRate.setOnClickListener(this)
        binding.aboutContent.cardRetroInfo.appShare.setOnClickListener(this)

        binding.aboutContent.cardSocial.telegramLink.setOnClickListener(this)
        binding.aboutContent.cardSocial.instagramLink.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.faqLink -> openUrl(Constants.FAQ_LINK)
            R.id.telegramLink -> openUrl(Constants.APP_TELEGRAM_LINK)
            R.id.appRate -> openUrl(Constants.RATE_ON_GOOGLE_PLAY)
            R.id.appShare -> shareApp()
            R.id.instagramLink -> openUrl(Constants.APP_INSTAGRAM_LINK)
        }
    }

    private fun getAppVersion(): String {
        return try {
            val packageInfo =
                requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "0.0.0"
        }
    }

    private fun shareApp() {
        ShareCompat.IntentBuilder(requireActivity()).setType("text/plain")
            .setChooserTitle(R.string.share_app)
            .setText(String.format(getString(R.string.app_share), requireActivity().packageName))
            .startChooser()
    }

    private fun loadContributors() {
        val contributorAdapter = ContributorAdapter(emptyList())
        binding.aboutContent.cardCredit.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            adapter = contributorAdapter
        }
        libraryViewModel.fetchContributors().observe(viewLifecycleOwner) { contributors ->
            contributorAdapter.swapData(contributors)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
