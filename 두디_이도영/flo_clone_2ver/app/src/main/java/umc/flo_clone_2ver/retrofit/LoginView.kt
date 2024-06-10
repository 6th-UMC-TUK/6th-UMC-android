package umc.flo_clone_2ver.retrofit

interface LoginView {
    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure(code: Int)
}